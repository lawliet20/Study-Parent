package com.wwj.service;

import com.wwj.dao.ResourceDao;
import com.wwj.model.pageModel.EasyUiMenu;
import com.wwj.model.pageModel.Menu;
import com.wwj.model.Resource;
import com.wwj.utils.CollectionUtil;
import org.apache.shiro.authz.permission.WildcardPermission;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 资源服务类
 * @author sherry
 * @since 1.0
 * @date 2016年10月18日16:42:25
 */
@Service("resourceService")
public class ResourceServiceImpl implements ResourceService {

    @javax.annotation.Resource
    private ResourceDao resourceDao;

    @Override
    public Resource createResource(Resource resource) {
        return resourceDao.createResource(resource);
    }

    @Override
    public Resource updateResource(Resource resource) {
        return resourceDao.updateResource(resource);
    }

    @Override
    public void deleteResource(Long resourceId) {
        resourceDao.deleteResource(resourceId);
    }

    @Override
    public Resource findOne(Long resourceId) {
        return resourceDao.findOne(resourceId);
    }

    @Override
    public List<Resource> findAll() {
        return resourceDao.findAll();
    }

    @Override
    public Set<String> findPermissions(Set<Long> resourceIds) {
        Set<String> permissions = new HashSet<String>();
        for (Long resourceId : resourceIds) {
            Resource resource = findOne(resourceId);
            if (resource != null && !StringUtils.isEmpty(resource.getIdentity())) {
                permissions.add(resource.getIdentity());
            }
        }
        return permissions;
    }

    /**
     * 查询当前用户用于的menu资源
     */
    @Override
    public List<EasyUiMenu> findMenus(Set<String> permissions) {
        //TODO 这里查询所有资源需要和用户权限比对，效率上有影响。后期可以配合缓存或者在查询时就过滤出用户拥有的资源
        List<Resource> allResources = findAll();
        List<Resource> menus = new ArrayList<Resource>();
        for (Resource resource : allResources) {
            if (resource.isRootNode()) {
                continue;
            }
            if (resource.getType() != Resource.ResourceType.menu) {
                continue;
            }
            if (!hasPermission(permissions, resource)) {
                continue;
            }
            menus.add(resource);
        }
        return convertToEasyUiMenu(menus);
    }

    private boolean hasPermission(Set<String> permissions, Resource resource) {
        if (StringUtils.isEmpty(resource.getIdentity())) {
            return true;
        }
        for (String permission : permissions) {
            WildcardPermission p1 = new WildcardPermission(permission);
            WildcardPermission p2 = new WildcardPermission(resource.getIdentity());
            if (p1.implies(p2) || p2.implies(p1)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 资源转换成easyui菜单
     * @param resources
     * @return
     */
    private static List<EasyUiMenu> convertToEasyUiMenu(List<Resource> resources){
        List<EasyUiMenu> menus = new ArrayList<>();
        for(Resource resource:resources){
            EasyUiMenu menu = convertToEasyUiMenu(resource);
            recursiveEasyUiMenu(menu,resources);
            menus.add(menu);
        }
        return menus;
    }

    /**
     * 资源转换成菜单
     * @param resources
     * @return
     */
    private static List<Menu> convertToMenus(List<Resource> resources){
        List<Menu> menus = new ArrayList<>();
        for(Resource resource:resources){
            Menu menu = convertToMenu(resource);
            recursiveMenu(menu,resources);
            menus.add(menu);
        }
        return menus;
    }

    /**
     * 递归找出menu及其子菜单
     */
    private static void recursiveMenu(Menu menu,List<Resource> resources){
        if(menu==null || CollectionUtil.isEmpty(resources)){
            return;
        }
        for(int i=resources.size();i>0;i--){
            Resource resource = resources.get(i);
            if(resource.getParentId().equals(menu.getId())){
                menu.getChildren().add(convertToMenu(resource));
                resources.remove(i);
            }
        }

        for(Menu childMenu:menu.getChildren()){
            recursiveMenu(childMenu,resources);
        }
    }

    private static Menu convertToMenu(Resource resource) {
        return new Menu(resource.getId(), resource.getName(), resource.getIcon(), resource.getUrl());
    }

    /**
     * 递归找出easyui menu及其子菜单
     */
    private static void recursiveEasyUiMenu(EasyUiMenu menu,List<Resource> resources){
        if(menu==null || CollectionUtil.isEmpty(resources)){
            return;
        }
        for(int i=resources.size();i>0;i--){
            Resource resource = resources.get(i);
            if(resource.getParentId().equals(menu.getId())){
                menu.getChildren().add(convertToEasyUiMenu(resource));
                resources.remove(i);
            }
        }

        for(EasyUiMenu childMenu:menu.getChildren()){
            recursiveEasyUiMenu(childMenu, resources);
        }
    }

    private static EasyUiMenu convertToEasyUiMenu(Resource resource){
        return new EasyUiMenu(resource.getId(), resource.getName(), resource.getIcon(), resource.getUrl());
    }
}
