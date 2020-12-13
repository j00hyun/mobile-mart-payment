package com.automart.service;

import com.automart.domain.Category;
import com.automart.domain.Product;
import com.automart.repository.CategoryRepository;
import com.automart.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    /***
     * 상품 등록하기
     * @param product : 등록할 상품
     * @return
     */
    @Transactional
    public Integer saveProduct(Product product) {
        productRepository.save(product);
        return product.getNo();
    }

    /***
     * 상품 수정하기
     * @param requestDto : 상품 수정 정보를 갖고있는 Dto
     * @return 수정된 상품에 대한 Dto
     */
    @Transactional
    public ProductResponseDto updateProduct(ProductUpdateRequestDto requestDto) {
        Category category = categoryRepository.findByNo(requestDto.getCategoryNo());
        Product product = productRepository.findByNo(requestDto.getProductNo())
                .orElseThrow(()->new IllegalStateException("상품이 존재하지 않습니다."));
        product = product.update(category, requestDto.getName(), requestDto.getPrice(),
                requestDto.getCost(), requestDto.getStock(), requestDto.getCode(), requestDto.getImgUrl(),
                requestDto.getLocation());
        return ProductResponseDto.of(product);
    }

    /***
     * 상품 제거하기
     * @param productNo : 제거할 상품에 대한 상품 고유번호
     */
    @Transactional
    public void removeProduct(Integer productNo) {
        Product product = productRepository.findByNo(productNo)
                .orElseThrow(()->new IllegalStateException("상품이 존재하지 않습니다."));
        productRepository.delete(product);
    }


    /***
     * 상품 단건 조회하기
     * @param productNo : 조회할 상품의 번호
     * @return 주문 단건에 대한 정보를 담은 Dto를 반환
     */
    public ProductResponseDto showProduct(Integer productNo) {
        Product product = productRepository.findByNo(productNo)
                .orElseThrow(()->new IllegalStateException("상품이 존재하지 않습니다."));
        return ProductResponseDto.of(product);
    }

    /***
     * 전체 상품 조회하기
     * @return 전체 상품에 대한 정보를 담은 Dto를 반환
     */
    public List<ProductResponseDto> showProducts() {
        List<Product> products = productRepository.findAll();
        return ProductResponseDto.listOf(products);
    }

}
