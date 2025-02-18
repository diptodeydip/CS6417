package com.example.OnlineMarketPlace.controller;

import com.example.OnlineMarketPlace.Commons;
import com.example.OnlineMarketPlace.DTO.ProductDTO;
import com.example.OnlineMarketPlace.database.ProductRepository;
import com.example.OnlineMarketPlace.database.UserRepository;
import com.example.OnlineMarketPlace.model.Product;
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

        try {
            Product product = new Product();
            product.setName(productDTO.getName());
            product.setDescription(productDTO.getDescription());
            product.setImage(productDTO.getImageFile().getBytes());
            product.setPrice(productDTO.getPrice());
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
        redirectAttributes.addFlashAttribute("messageType", "flash-success");
        return "redirect:/products";
    }

    @GetMapping("products/delete/{id}")
    public String deleteProduct(@PathVariable Long id, RedirectAttributes redirectAttributes, HttpSession session) {
        Optional<Product> product = productRepo.findById(id);
        if (product.isPresent() && session.getAttribute(Commons.userId) == product.get().getOwnerId()) {
            productRepo.deleteById(id);
            redirectAttributes.addFlashAttribute("message", "Product deleted successfully!");
            redirectAttributes.addFlashAttribute("messageType", "flash-success");

        }else {
            redirectAttributes.addFlashAttribute("message", "Not authorized to delete this product");
            redirectAttributes.addFlashAttribute("messageType", "flash-error");
        }
        return "redirect:/products";
    }

    @GetMapping("products/edit/{id}")
    public String editProduct(@PathVariable Long id, RedirectAttributes redirectAttributes, HttpSession session, Model model) {
        Optional<Product> product = productRepo.findById(id);
        if (product.isPresent() && session.getAttribute(Commons.userId) == product.get().getOwnerId()) {
            ProductDTO productDTO = new ProductDTO();
            productDTO.setId(product.get().getId());
            productDTO.setName(product.get().getName());
            productDTO.setDescription(product.get().getDescription());
            productDTO.setPrice(product.get().getPrice());
            model.addAttribute(productDTO);
            return "edit_product_page";


        }else {
            redirectAttributes.addFlashAttribute("message", "Not authorized to edit this product");
            redirectAttributes.addFlashAttribute("messageType", "flash-error");
        }
        return "redirect:/products";
    }


    @PostMapping("editProduct")
    public String editProduct(Model model, @Valid @ModelAttribute ProductDTO productDTO
            , BindingResult result, HttpSession session, RedirectAttributes redirectAttributes) {

        try {
            Product product = new Product();
            product.setId(productDTO.getId());
            product.setName(productDTO.getName());
            product.setDescription(productDTO.getDescription());
            product.setImage(productDTO.getImageFile().getBytes());
            product.setPrice(productDTO.getPrice());
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
        redirectAttributes.addFlashAttribute("messageType", "flash-success");
        return "redirect:/products";
    }
}
