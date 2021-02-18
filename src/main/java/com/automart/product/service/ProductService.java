package com.automart.product.service;

import com.automart.advice.exception.DateFormatNotValidException;
import com.automart.advice.exception.MultipartException;
import com.automart.advice.exception.NotFoundDataException;
import com.automart.category.domain.Category;
import com.automart.category.repository.CategoryRepository;
import com.automart.product.domain.Product;
import com.automart.product.dto.ProductResponseDto;
import com.automart.product.dto.ProductSaveRequestDto;
import com.automart.product.dto.ProductUpdateRequestDto;
import com.automart.product.repository.ProductRepository;
import com.automart.s3.Uploader;
import com.automart.subdivision.domain.Subdivision;
import com.automart.subdivision.repository.SubdivisionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.text.ParseException;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final SubdivisionRepository subdivisionRepository;
    private final Uploader uploader;


    /**
     * 상품 등록하기
     * @param requestDto : 등록할 상품에 대한 정보를 갖고있는 Dto
     * @return 등록한 상품 식별자
     */
    @Transactional
    public ProductResponseDto saveProduct(ProductSaveRequestDto requestDto) throws Exception {
        Product product = null;
        // 파일 저장 경로 : products/{categoryNo}/{productNo}
        String dirName = "products/" + requestDto.getCategoryNo();

        Category category = categoryRepository.findByNo(requestDto.getCategoryNo())
                .orElseThrow(() -> new NotFoundDataException("해당 카테고리가 존재하지 않습니다."));

        Subdivision subdivision = subdivisionRepository.findByNo(requestDto.getSubdivNo())
                .orElseThrow(() -> new NotFoundDataException("해당 소분류가 존재하지 않습니다."));
        try {
            product = Product.createProduct(category, subdivision, requestDto.getName(),
                    requestDto.getPrice(), requestDto.getCost(), requestDto.getStock(),
                    requestDto.getMinStock(), requestDto.getReceivingDate(), requestDto.getCode(), requestDto.getLocation());

            productRepository.save(product);
            uploader.upload(requestDto.getImg(), dirName, Integer.toString(product.getNo()));

        } catch (ParseException e) {
            throw new DateFormatNotValidException("마지막 입고 날짜의 형식이 올바르지 않습니다.");
        } catch (IOException e) {
            productRepository.delete(product);
            throw new MultipartException("이미지 업로드에 실패하였습니다.");
        }

        product.setImgUrl(dirName + "/" + product.getNo());
        productRepository.save(product);
        return ProductResponseDto.of(product);
    }


    /**
     * 상품 수정하기
     * @param requestDto : 상품 수정 정보를 갖고있는 Dto
     * @return 수정된 상품에 대한 Dto
     */
    @Transactional
    public ProductResponseDto updateProduct(ProductUpdateRequestDto requestDto) throws Exception {
        Product product = productRepository.findByNo(requestDto.getProductNo())
                .orElseThrow(()->new NotFoundDataException("상품이 존재하지 않습니다."));
        int categoryNo = product.getCategory().getNo();
        String dirName = "products/" + categoryNo;

        try {
            product = product.update(requestDto.getName(), requestDto.getPrice(),
                    requestDto.getCost(), requestDto.getStock(), requestDto.getMinStock(),
                    requestDto.getReceivingDate(), requestDto.getCode(), requestDto.getLocation());

            // 이미지가 변경되었을 때만 S3 접근
            if(!requestDto.getImg().isEmpty()) {
                uploader.upload(requestDto.getImg(), dirName, Integer.toString(product.getNo()));
            }

        } catch (ParseException e) {
            throw new DateFormatNotValidException ("마지막 입고 날짜의 형식이 올바르지 않습니다.");
        } catch (IOException e) {
            throw new MultipartException("이미지 업로드에 실패하였습니다.");
        }

        productRepository.save(product);
        return ProductResponseDto.of(product);
    }


    /**
     * 상품 제거하기
     * @param productNo : 제거할 상품에 대한 상품 고유번호
     */
    @Transactional
    public void removeProduct(Integer productNo) throws NotFoundDataException {
        Product product = productRepository.findByNo(productNo)
                .orElseThrow(()->new NotFoundDataException("상품이 존재하지 않습니다."));

        uploader.delete(product.getImgUrl());
        productRepository.delete(product);
    }

}
