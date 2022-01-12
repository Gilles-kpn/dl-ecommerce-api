package fr.gilles.auth.services.product;

import fr.gilles.auth.entities.picture.Picture;
import fr.gilles.auth.entities.products.Product;
import fr.gilles.auth.entities.rating.Like;
import fr.gilles.auth.entities.user.Admin;
import fr.gilles.auth.entities.user.User;
import fr.gilles.auth.payloader.query.QueryParams;
import fr.gilles.auth.repositories.LikeRepository;
import fr.gilles.auth.repositories.ProductRepository;
import fr.gilles.auth.services.files.FileService;
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


    public Page<Product> all(QueryParams pageable){
        return  productRepository.findByDeleted(pageable.isDeleted(),pageable.toPageRequest());
    }


    public Page<Product> search(@NotNull @NotEmpty @NotBlank String name, QueryParams params){
        return  productRepository.findByNameIsLikeAndDeleted(name, params.toPageRequest(), params.isDeleted() );
    }

    public Optional<Product> find(@NotNull @NotEmpty @NotBlank  String code){
        return  productRepository.findByCode(code);
    }

    public Collection<Picture> uploadImages(@NotNull @NotEmpty Collection<MultipartFile> multipartFiles) throws IOException {
        Collection<Picture> pictures = new ArrayList<>();
        for(MultipartFile file : multipartFiles){
            Picture temp = new Picture();
            Map result = fileService.uploadImage("auth/product/images/"+file.getOriginalFilename(), file);
            temp.setUrl((String)result.get("url"));
            //do same for other fields like public_id, width, height, format , resource_type, version, signature
            temp.setPublic_id((String)result.get("public_id"));
            temp.setWidth((Integer)result.get("width"));
            temp.setHeight((Integer)result.get("height"));
            temp.setFormat((String)result.get("format"));
            temp.setResource_type((String)result.get("resource_type"));
            temp.setVersion((Integer)result.get("version"));
            temp.setSignature((String)result.get("signature"));
            pictures.add(temp);
        }
        return pictures;
    }


    public Product updateAvailability(@NotNull @NotEmpty @NotBlank String code,boolean available) throws NotFoundException{
        Optional<Product> product = find(code);
        if(product.isPresent()){
            product.get().setAvailable(true);
            return productRepository.save(product.get());
        }
        throw new NotFoundException("Product not found");
    }


    public void delete(@NotNull @NotEmpty @NotBlank String code) throws NotFoundException{
        Optional<Product> product = find(code);
        if(product.isPresent()){
            product.get().setDeleted(true);
            productRepository.save(product.get());
        }else{
            throw new NotFoundException("Product not found");
        }
    }

    public Product recycle(@NotNull @NotEmpty @NotBlank String code) throws NotFoundException{
        Optional<Product> product = find(code);
        if(product.isPresent()){
            product.get().setDeleted(false);
            return productRepository.save(product.get());
        }
        throw new NotFoundException("Product not found");
    }


    public void like(@NotNull Product product,@NotNull User user){
        if(likeRepository.findByProductAndUser(product,user).isEmpty()){
            Like like = new Like();
            like.setProduct(product);
            like.setUser(user);
            likeRepository.save(like);
        }
    }

    public void unlike(@NotNull Product product, @NotNull User user){
        likeRepository.findByProductAndUser(product,user).ifPresent(likeRepository::delete);
    }

    public Page<Product> fromSameAuthor( @NotNull Admin admin, QueryParams queryParams){
        return productRepository.findByAuthorAndDeleted(admin, queryParams.isDeleted(), queryParams.toPageRequest());
    }



}
