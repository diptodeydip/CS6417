package com.example.OnlineMarketPlace.controller;

import com.example.OnlineMarketPlace.Commons;
import com.example.OnlineMarketPlace.DTO.ProductDTO;
import com.example.OnlineMarketPlace.database.ProductRepository;
import com.example.OnlineMarketPlace.database.UserRepository;
import com.example.OnlineMarketPlace.model.Product;
import com.example.OnlineMarketPlace.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;

@Controller
@RequestMapping("/")
public class ProductController {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private ProductRepository productRepo;

    @Autowired
    UserService userService;

    @GetMapping("products")
    public String index(Model model) {
        model.addAttribute("products", productRepo.findAll());
        return "product_list";
    }

    @ResponseBody //this annotation returns textual response
    @GetMapping("products/image/{id}")
    public ResponseEntity<byte[]> fetchImage(@PathVariable Long id) {
        Optional<Product> product = productRepo.findById(id);
        if (product.isPresent() && product.get().getImage() != null) {
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, "image/jpeg")
                    .body(product.get().getImage());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("addProductPage")
    public String addProduct(Model model) {
        ProductDTO productDTO = new ProductDTO();
        model.addAttribute(productDTO);
        return "add_product_page";
    }

    @PostMapping("addProduct")
    public String addProduct(Model model, @Valid @ModelAttribute ProductDTO productDTO
            , BindingResult result, HttpSession session, RedirectAttributes redirectAttributes) {

        String contentType = productDTO.getImageFile().getContentType();
        if (contentType == null || (!contentType.equals("image/jpeg") && !contentType.equals("image/png") && !contentType.equals("image/jpg"))) {
            result.addError(
                    new FieldError("productDTO", "imageFile",
                           "unsupported file")
            );
            return "add_product_page"; // Redirect back to the upload page
        }

        try {
            Product product = new Product();
            product.setName(productDTO.getName());
            product.setDescription(productDTO.getDescription());
            product.setImage(productDTO.getImageFile().getBytes());
            product.setPrice(productDTO.getPrice());
            product.setContact(productDTO.getContact());
            product.setOwnerId((Long) session.getAttribute(Commons.userId));
            product.setCreatedAt(new Date());

            productRepo.save(product);

        }catch (Exception e){
            result.addError(
                    new FieldError("productDTO", "name",
                            e.getMessage())
            );
            return "add_product_page";
        }

        redirectAttributes.addFlashAttribute("message", "Product uploaded successfully!");
        return "redirect:/products";
    }

    @GetMapping("products/delete/{id}")
    public String deleteProduct(@PathVariable Long id, RedirectAttributes redirectAttributes, HttpSession session) {
        Optional<Product> product = productRepo.findById(id);
        if (userService.isSameUser(product, session) || (userService.isAdmin((String) session.getAttribute(Commons.role)) && product.isPresent())) {
            productRepo.deleteById(id);
            redirectAttributes.addFlashAttribute("message", "Product deleted successfully!");

        }else {
            redirectAttributes.addFlashAttribute("message", "Not authorized to delete this product");
        }
        return "redirect:/products";
    }

    @GetMapping("products/edit/{id}")
    public String editProduct(@PathVariable Long id, RedirectAttributes redirectAttributes, HttpSession session, Model model) {
        Optional<Product> product = productRepo.findById(id);
        if (userService.isSameUser(product, session)) {
            ProductDTO productDTO = new ProductDTO();
            productDTO.setId(product.get().getId());
            productDTO.setName(product.get().getName());
            productDTO.setContact(product.get().getContact());
            productDTO.setDescription(product.get().getDescription());
            productDTO.setPrice(product.get().getPrice());
            model.addAttribute(productDTO);
            return "edit_product_page";


        }else {
            redirectAttributes.addFlashAttribute("message", "Not authorized to edit this product");
        }
        return "redirect:/products";
    }


    @PostMapping("editProduct")
    public String editProduct(Model model, @Valid @ModelAttribute ProductDTO productDTO
            , BindingResult result, HttpSession session, RedirectAttributes redirectAttributes) {

        String contentType = productDTO.getImageFile().getContentType();
        if (contentType == null || (!contentType.equals("image/jpeg") && !contentType.equals("image/png") && !contentType.equals("image/jpg"))) {
            result.addError(
                    new FieldError("productDTO", "imageFile",
                            "unsupported file")
            );
            return "edit_product_page"; // Redirect back to the upload page
        }

        try {
            Product product = new Product();
            product.setId(productDTO.getId());
            product.setName(productDTO.getName());
            product.setDescription(productDTO.getDescription());
            product.setImage(productDTO.getImageFile().getBytes());
            product.setPrice(productDTO.getPrice());
            product.setContact(productDTO.getContact());
            product.setOwnerId((Long) session.getAttribute(Commons.userId));
            product.setCreatedAt(new Date());

            productRepo.save(product);

        }catch (Exception e){
            result.addError(
                    new FieldError("productDTO", "name",
                            e.getMessage())
            );
            return "edit_product_page";
        }

        redirectAttributes.addFlashAttribute("message", "Product edited successfully!");
        return "redirect:/products";
    }
}
