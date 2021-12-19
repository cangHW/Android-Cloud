package com.proxy.service.media.info;

import com.proxy.service.annotations.CloudApiNewInstance;
import com.proxy.service.annotations.CloudApiService;
import com.proxy.service.api.services.CloudMediaRecorderService;
import com.proxy.service.api.tag.CloudServiceTagMedia;

/**
 * @author : cangHX
 * on 2021/06/04  9:32 PM
 */
@CloudApiNewInstance
@CloudApiService(serviceTag = CloudServiceTagMedia.MEDIA_RECORDER)
public class MediaRecorderServiceImpl implements CloudMediaRecorderService {
}
