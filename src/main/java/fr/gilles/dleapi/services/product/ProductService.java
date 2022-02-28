package fr.gilles.dleapi.services.product;

import fr.gilles.dleapi.entities.picture.Picture;
import fr.gilles.dleapi.entities.products.Category;
import fr.gilles.dleapi.entities.products.Product;
import fr.gilles.dleapi.entities.rating.Like;
import fr.gilles.dleapi.entities.user.Admin;
import fr.gilles.dleapi.entities.user.User;
import fr.gilles.dleapi.payloader.query.ProductQueryParams;
import fr.gilles.dleapi.payloader.response.CountStats;
import fr.gilles.dleapi.payloader.response.ExceptionMessage;
import fr.gilles.dleapi.repositories.LikeRepository;
import fr.gilles.dleapi.repositories.ProductRepository;
import fr.gilles.dleapi.services.files.FileService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;
import org.webjars.NotFoundException;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.*;

@Service
@AllArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final FileService fileService;
    private final LikeRepository likeRepository;

    public Product create(@NotNull @Validated Product toCreate){
        if(productRepository.findByCode(toCreate.getCode()).isEmpty()){
            return productRepository.save(toCreate);
        }else{
            toCreate.setCode(UUID.randomUUID().toString());
            return  create(toCreate);
        }
    }


    public Page<Product> all(ProductQueryParams pageable, Set<Category> categories){
        return  productRepository.findByDeletedOrCategoryIsInOrPriceBetweenAndStatus(pageable.isDeleted(),categories,
                pageable.getPriceMin(), pageable.getPriceMax()  ,pageable.toPageRequest(),pageable.getStatus());
    }


    public Page<Product> search(@NotNull @NotEmpty @NotBlank String name, ProductQueryParams params){
        return  productRepository.findByNameContainsAndDeletedAndStatus(name, params.toPageRequest(), params.isDeleted(),params.getStatus() );
    }

    public Page<Product> search(@NotNull @NotEmpty @NotBlank String name, ProductQueryParams params, Admin admin){
        return productRepository.findByNameContainsAndDeletedAndAuthorAndStatus(name,params.isDeleted(),admin, params.toPageRequest(),params.getStatus());
    }

    public Optional<Product> find(@NotNull @NotEmpty @NotBlank  String code){
        return  productRepository.findByCode(code);
    }

    public Collection<Picture> uploadImages(@NotNull @NotEmpty Collection<MultipartFile> multipartFiles) throws IOException {
        Collection<Picture> pictures = new ArrayList<>();
        for(MultipartFile file : multipartFiles){
            Picture temp = new Picture();
            Map<String,Object> result = fileService.uploadImage("auth/product/images/"+System.currentTimeMillis()+"_"+file.getOriginalFilename(), file);
            temp.setUrl((String)result.get("url"));
            temp.setPublicId((String)result.get("public_id"));
            temp.setWidth((Integer)result.get("width"));
            temp.setHeight((Integer)result.get("height"));
            temp.setFormat((String)result.get("format"));
            temp.setResourceType((String)result.get("resource_type"));
            temp.setVersion((Integer)result.get("version"));
            temp.setSignature((String)result.get("signature"));
            pictures.add(temp);
        }
        return pictures;
    }


    public Product updateAvailability(@NotNull @NotEmpty @NotBlank String code,boolean available) throws NotFoundException{
        Optional<Product> product = find(code);
        if(product.isPresent()){
            product.get().setAvailable(available);
            return productRepository.save(product.get());
        }
        throw new NotFoundException(ExceptionMessage.PRODUCT_NOT_FOUND);
    }


    public void delete(@NotNull @NotEmpty @NotBlank String code) throws NotFoundException{
        Optional<Product> product = find(code);
        if(product.isPresent()){
            product.get().setDeleted(true);
            productRepository.save(product.get());
        }else{
            throw new NotFoundException(ExceptionMessage.PRODUCT_NOT_FOUND);
        }
    }

    public Product recycle(@NotNull @NotEmpty @NotBlank String code) throws NotFoundException{
        Optional<Product> product = find(code);
        if(product.isPresent()){
            product.get().setDeleted(false);
            return productRepository.save(product.get());
        }
        throw new NotFoundException(ExceptionMessage.PRODUCT_NOT_FOUND);
    }


    public void like(@NotNull Product product,@NotNull User user){
        if(likeRepository.findByProductAndUser(product,user).isEmpty()){
            Like like = new Like();
            like.setProduct(product);
            like.setUser(user);
            likeRepository.save(like);
        }
    }

    public Product save( @Validated @NotNull Product product){
        return  productRepository.save(product);
    }

    public void unlike(@NotNull Product product, @NotNull User user){
        likeRepository.findByProductAndUser(product,user).ifPresent(likeRepository::delete);
    }

    public Page<Product> fromAuthor(@NotNull Admin admin, ProductQueryParams queryParams){
        return productRepository.findByAuthorAndDeletedAndStatus(admin, queryParams.isDeleted(), queryParams.toPageRequest(),queryParams.getStatus());
    }


    public Page<Product> findByCategories(Set<Category> categories,ProductQueryParams queryParams){
        return  productRepository.findByCategoryIsInAndDeletedAndStatus(categories,queryParams.isDeleted(), queryParams.toPageRequest(),queryParams.getStatus());
    }

    public int count(boolean deleted){
        return  productRepository.countAllByDeleted(deleted);
    }


    public List<CountStats> createdStats(Date start, Date end){
        return  productRepository.createdStats(start, end);
    }

    public List<CountStats> deletedStats(Date start, Date end){
        return  productRepository.deletedStats(start, end);
    }




}
