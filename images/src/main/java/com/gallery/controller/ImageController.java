package com.gallery.controller;

import com.gallery.core.request.CreateImageRequest;
import com.gallery.core.request.UpdateImageDataRequest;
import com.gallery.core.response.UploadImageResponse;
import com.gallery.service.ImageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/image")
@CrossOrigin
public class ImageController {
  @Autowired
  ImageService imageService;

  // creates gridfs image but empty image document.
  // @RequestMapping(value = "/upload", method = RequestMethod.POST)
  // public ResponseEntity<UploadImageResponse> uploadImage(@RequestParam("file") MultipartFile file) {
  //     System.out.println(
  //             "Image uploaded: " + file.getOriginalFilename() + " " + file.getContentType() + " " + file.getSize());
  //     return imageService.createImage(null, file);
  // }

  // creates gridfs image and fill image document.
  @RequestMapping(value = "/upload", method = RequestMethod.POST)
  public ResponseEntity<UploadImageResponse> uploadImage(@ModelAttribute CreateImageRequest createRequest) {
    System.out.println("uploading image.");
    return imageService.createImage(null, createRequest);
  }

  @RequestMapping(value = "/{imageId}", method = RequestMethod.PUT)
  public ResponseEntity<?> updateImageData(@RequestBody UpdateImageDataRequest updateRequest,
      @PathVariable("imageId") String imageId) {
    return imageService.updateImageData(imageId, updateRequest);
  }

  @RequestMapping(value = "/increment/{imageId}", method = RequestMethod.PUT)
  public ResponseEntity<?> incrementImageTotalViews(@PathVariable("imageId") String imageId) {
    return imageService.incrementImageTotalViews(imageId);
  }

  @RequestMapping(value = "/download/{imageId}", method = RequestMethod.GET)
  public ResponseEntity<?> downloadImage(@PathVariable("imageId") String imageId) {
    return imageService.retrieveImage(imageId);
  }

  @RequestMapping(value = "/{imageId}", method = RequestMethod.GET)
  public ResponseEntity<?> getImageData(@PathVariable("imageId") String imageId) {
    return imageService.getImageData(imageId);
  }

  @RequestMapping(value = "/recent/{numOfImages}", method = RequestMethod.GET)
  public ResponseEntity<?> getRecentImageData(@PathVariable("numOfImages") int numOfImages) {
    return imageService.getRecentImageData(numOfImages);
  }

  @RequestMapping(value = "/user/{userId}", method = RequestMethod.GET)
  public ResponseEntity<?> getImagesDataByUserId(@PathVariable("userId") String userId) {
    return imageService.getImagesDataByUserId(userId);
  }

  @RequestMapping(value = "/{imageId}", method = RequestMethod.DELETE)
  public ResponseEntity<?> deleteImage(@PathVariable("imageId") String imageId) {
    return imageService.deleteImage(imageId);
  }
}
