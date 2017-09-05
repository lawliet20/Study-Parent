package org.simpleframe.beans.factory.config;

import org.simpleframe.beans.BeanMetadataElement;

/**
 * Created by sherry on 16/9/6.
 */
public interface BeanDefinition extends BeanMetadataElement {

    String SCOPE_SINGLETON = "singleton";

    String SCOPE_PROTOTYPE = "prototype";

    int ROLE_APPLICATION = 0;

    int ROLE_SUPPORT = 1;

    int ROLE_INFRASTRUCTURE = 2;

    String getBeanName();

    String getBeanClassName();

    void setBeanClassName(String beanClassName);

    String getScope();

    void setScope(String scope);

    String getRole();

    void setRole();

    boolean isLazyInit();

    void setLazyInit(boolean lazyInit);

    String[] getDependsOn();

    void setDepends(String[] dependsOn);

    ConstructorArgumentValues getConstructorArgumentValues();

    void setConstructorArgumentValues(ConstructorArgumentValues constructorArgumentValues);

    boolean isSingleton();

    boolean isPrototype();

    boolean isAbstract();

    String getDescription();

    String getResourceDescription();

    BeanDefinition getOriginatingBeanDefinition();


}
