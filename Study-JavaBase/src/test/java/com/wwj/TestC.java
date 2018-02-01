package com.wwj;


import com.alibaba.fastjson.JSON;
import org.hibernate.service.spi.ServiceException;
import org.springframework.http.*;
import org.springframework.util.MultiValueMap;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.RestTemplate;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 *
 */
public class TestC {
    public static void main(String[] args) throws IOException, LineUnavailableException, UnsupportedAudioFileException, ExecutionException, InterruptedException {
//        testUrl();
//        test2();

    }

    public static void testUrl() throws ExecutionException, InterruptedException {
        String accessToken = "6_7VhmDM9TVkx1VYosoqRDm1Qc4bJqF2egpOtpdIHnU8bAOrW8vxLLSXVUNvtYwaDyAdSgqe0nRG67GQGLhGTLTHFLCkQQwj2-BAIoDWBm-RqJVZLOS5DI_Pmcr3twDxz0HTIFl-_Oi2mPOG0BVOUiAIAIBH";
        String url = "https://api.weixin.qq.com/cgi-bin/material/get_material?access_token="+accessToken;
//        String url = "https://api.weixin.qq.com/cgi-bin/material/get_material?access_token=6_C44B-PbjS_e4lV5tGyKx11Y2x9jkyxbgtri06WFA9VI1ipf1MpBrHmtJKbRnOsE9WTkzqZTpkv2fatJYVVpac-XiR0gkynOOgqcCSmTYFA0jMJGMKEwbmchiYZU3rjkObxR7y-roZUed1pelNKJiACAGDC";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        Map<String,String> params = new HashMap();
        params.put("media_id","VrucACxs6wZI5jZMkSY4uwdqlgDl1SL9Q0dsDFIJKQw");
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity(JSON.toJSONString(params),headers);
        RestTemplate asyncRestTemplate = new RestTemplate();
        ResponseEntity<byte[]> future = asyncRestTemplate.exchange(url, HttpMethod.POST, requestEntity, byte[].class);
        System.out.println(future.getBody().length);
        System.out.println(future);
        System.out.println(future.getHeaders());
        System.out.println(future.getHeaders().getContentType().getSubtype());


//        String res = HttpUtil.post(url,JSON.toJSONString(params));
//        System.out.println(res);
    }

    public static void test2(){
        try {
//            String accessToken = "6_4vo4ymLzI75TGHj3nmv-rMqTkTFfdjWK024_zrRL5kdEn1b8hROJIsshDauKqmnnrMmpKIoSwIGZZjdCvNvCOOC93F33pbDVYMihDfbmFALoDfFHBEew2cIGb4TqSFxygiMyid8vL_217_RXRXKdAIALRE";
            String url = "https://api.weixin.qq.com/cgi-bin/material/get_material?access_token=6_C44B-PbjS_e4lV5tGyKx11Y2x9jkyxbgtri06WFA9VI1ipf1MpBrHmtJKbRnOsE9WTkzqZTpkv2fatJYVVpac-XiR0gkynOOgqcCSmTYFA0jMJGMKEwbmchiYZU3rjkObxR7y-roZUed1pelNKJiACAGD";
            Map<String,String> params = new HashMap();
            params.put("media_id","huz-UTRIvan7Hqhpa8AHJFupBvHukmegeCph_ejOSBA");
//            HttpHeaders headers = new HttpHeaders();
//            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            AsyncRestTemplate asyncRestTemplate = new AsyncRestTemplate();
//            ListenableFuture<ResponseEntity<String>> future = asyncRestTemplate.exchange(url, HttpMethod.GET,
//                    new HttpEntity<>(JSON.toJSONString(params), headers), String.class);
////            ResponseEntity<byte[]> res = future.get();
//            System.out.println(JSON.toJSONString(future.get()));



            HttpHeaders headers = new HttpHeaders();
            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity(params, headers);
            ListenableFuture<ResponseEntity<byte[]>> future = asyncRestTemplate.exchange(url, HttpMethod.POST, requestEntity, byte[].class);
            byte[] res = future.get().getBody();
            System.out.println(res.length);
        } catch (Exception e) {
            throw new ServiceException("error.client.mirrorGetError", e);
        }
    }

    /**
     * 获取wav格式音频文件的长度
     *
     * @param file
     * @return
     */
    public Integer getWavDuration(File file) {
        try {
            Clip clip = AudioSystem.getClip();
            AudioInputStream ais = AudioSystem.getAudioInputStream(file);
            clip.open(ais);
            return (int) Math.ceil(clip.getMicrosecondLength() / 1000000D);
        } catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取amr格式音频长度
     *
     * @param file
     * @return
     * @throws IOException
     */
    public static Integer getAmrDuration(File file) throws IOException {
        long duration = -1;
        int[] packedSize = {12, 13, 15, 17, 19, 20, 26, 31, 5, 0, 0, 0, 0, 0, 0, 0};
        RandomAccessFile randomAccessFile = null;
        try {
            randomAccessFile = new RandomAccessFile(file, "rw");
            long length = file.length();// 文件的长度
            int pos = 6;// 设置初始位置
            int frameCount = 0;// 初始帧数
            int packedPos = -1;

            byte[] datas = new byte[1];// 初始数据值
            while (pos <= length) {
                randomAccessFile.seek(pos);
                if (randomAccessFile.read(datas, 0, 1) != 1) {
                    duration = length > 0 ? ((length - 6) / 650) : 0;
                    break;
                }
                packedPos = (datas[0] >> 3) & 0x0F;
                pos += packedSize[packedPos] + 1;
                frameCount++;
            }

            duration += frameCount * 20;// 帧数*20
            return (int) ((duration / 1000) + 1);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (randomAccessFile != null) {
                randomAccessFile.close();
            }
        }
        return 0;
    }

    /**
     * 获取MP3音频长度
     *
     * @param file
     * @return
     *//*

    public static int getMp3Duration(File file) {
        int length = 0;
        try {
            MP3File mp3File = (MP3File) AudioFileIO.read(file);
            MP3AudioHeader audioHeader = (MP3AudioHeader) mp3File.getAudioHeader();
            // 单位为秒
            length = audioHeader.getTrackLength();
            return length;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return length;
    }

    public void uuidTest() {

        UUIDGenerator generator = UUIDGenerator.getInstance();

        EthernetAddress address = generator.getDummyAddress();
        System.out.println(address.toString());
        UUID uuid = generator.generateRandomBasedUUID();
        System.out.println(uuid.toString());
        uuid = generator.generateTimeBasedUUID();
        System.out.println(uuid.toString());
    }*/
}

