package com.automart.s3;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * 현재 AWS S3 Bucket을 Storage로 사용하여 개발중이지만, 차후 변경 & 확장을 대비하여
 * 객체간의 의존성을 줄이기 위해 Uploader라는 인터페이스를 사용
 */
public interface Uploader {

    String upload(MultipartFile multipartFile, String dirName, String fileName) throws IOException;

}
