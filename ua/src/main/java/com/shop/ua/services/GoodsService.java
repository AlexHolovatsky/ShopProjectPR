package com.shop.ua.services;

import com.shop.ua.models.Goods;
import com.shop.ua.models.Image;
import com.shop.ua.repositories.GoodsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class GoodsService{
    private final GoodsRepository goodsRepository;

    public List<Goods> listGoods(String title){
        if (title != null) return goodsRepository.findByTitle(title);
        return goodsRepository.findAll();
    }

    public List<Goods> listUnapprovedGoods() {
        return goodsRepository.findByApproved(false);
    }

    public List<Goods> listApprovedGoods() {
        return goodsRepository.findByApproved(true);
    }

    public void saveGoods(Goods goods, MultipartFile file1, MultipartFile file2, MultipartFile file3) throws IOException {
        Image image1;
        Image image2;
        Image image3;
        if (file1.getSize() != 0){
            image1 = toImageEntity(file1);
            image1.setPreviewImage(true);
            goods.addImageToGoods(image1);
        }
        if (file2.getSize() != 0){
            image2 = toImageEntity(file2);
            goods.addImageToGoods(image2);
        }
        if (file3.getSize() != 0){
            image3 = toImageEntity(file3);
            goods.addImageToGoods(image3);
        }

        log.info("Saving new Good. Title: {}; Price: {}", goods.getTitle(), goods.getPrice());
        goods.setApproved(false);
        Goods goodsFromDb = goodsRepository.save(goods);
        goodsFromDb.setPreviewImageId(goodsFromDb.getImages().get(0).getId());
        goodsRepository.save(goods);
    }

    private Image toImageEntity(MultipartFile file) throws IOException {
        Image image = new Image();
        image.setName(file.getName());
        image.setOriginalFileName(file.getOriginalFilename());
        image.setContentType(file.getContentType());
        image.setSize(file.getSize());
        image.setBytes(file.getBytes());
        return image;
    }


    public void deleteGoods(Long id){
        goodsRepository.deleteById(id);
    }

    public Goods getGoodsById(Long id){
        return goodsRepository.findById(id).orElse(null);
    }

    public void approveGoods(Long id) {
        Goods goods = goodsRepository.findById(id).orElse(null);
        if (goods != null) {
            goods.setApproved(true);
            goodsRepository.save(goods);
        }
    }


    public void rejectProduct(Long id) {
        goodsRepository.deleteById(id);
    }
}
