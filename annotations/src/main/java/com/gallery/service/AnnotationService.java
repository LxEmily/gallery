package com.gallery.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import com.gallery.config.MongoConfig;
import com.gallery.constants.Constants;
import com.gallery.core.response.AnnotationResponse;
import com.gallery.core.response.AnnotationsResponse;
import com.gallery.model.Annotation;
import com.gallery.model.RectangleCoordinates;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class AnnotationService {
  private MongoTemplate mongoTemplate;
  @Autowired
  private MongoConfig mongoConfig;

  public AnnotationService() {

  }

  @PostConstruct
  private void init() throws Exception {
    mongoTemplate = mongoConfig.mongoTemplate();
  }

  public ResponseEntity<AnnotationResponse> createAnnotation(String userId, String imageId,
      RectangleCoordinates coordinates, String content) {

    Annotation annotation = new Annotation(userId, imageId, coordinates, content, LocalDateTime.now());

    mongoTemplate.insert(annotation, Constants.ANNOTATION_COLLECTION);

    return new ResponseEntity<>(new AnnotationResponse("Annotation created", annotation), HttpStatus.CREATED);
  }

  public ResponseEntity<AnnotationResponse> getAnnotation(String annotationId) {
    Annotation annotation = mongoTemplate.findOne(Query.query(Criteria.where("annotationId").is(annotationId)),
        Annotation.class, Constants.ANNOTATION_COLLECTION);

    if (annotation == null) {
      return new ResponseEntity<>(new AnnotationResponse("Annotation does not exist", null), HttpStatus.NOT_FOUND);
    }

    return new ResponseEntity<>(new AnnotationResponse("Retrieved annotation", annotation), HttpStatus.OK);
  }

  public ResponseEntity<AnnotationsResponse> getAllAnnotationsByImageId(String imageId) {
    List<Annotation> annotations = mongoTemplate.find(Query.query(Criteria.where("imageId").is(imageId)),
        Annotation.class, Constants.ANNOTATION_COLLECTION);

    if (annotations == null) {
      return new ResponseEntity<>(new AnnotationsResponse("Annotations does not exist", null), HttpStatus.NOT_FOUND);
    }

    return new ResponseEntity<>(new AnnotationsResponse("Retrieved annotation", annotations), HttpStatus.OK);
  }

  public ResponseEntity<AnnotationsResponse> getAllAnnotationsByUserId(String userId) {
    List<Annotation> annotations = mongoTemplate.find(Query.query(Criteria.where("userId").is(userId)),
        Annotation.class, Constants.ANNOTATION_COLLECTION);

    if (annotations == null) {
      return new ResponseEntity<>(new AnnotationsResponse("Annotations does not exist", null), HttpStatus.NOT_FOUND);
    }

    return new ResponseEntity<>(new AnnotationsResponse("Retrieved annotation", annotations), HttpStatus.OK);
  }

  public List<String> getAllImageIdByAnnotations(List<Annotation> list) {
    List<String> imageId = new ArrayList<>();
    for (Annotation annotation : list) {
      imageId.add(annotation.getImageId());
    }

    return imageId;
  }

  public ResponseEntity<AnnotationsResponse> getAllAnnotations() {
    List<Annotation> annotations = mongoTemplate.findAll(Annotation.class, Constants.ANNOTATION_COLLECTION);

    if (annotations == null) {
      return new ResponseEntity<>(new AnnotationsResponse("Annotations does not exist", null), HttpStatus.NOT_FOUND);
    }

    return new ResponseEntity<>(new AnnotationsResponse("Retrieved annotations", annotations), HttpStatus.OK);
  }

  public ResponseEntity<AnnotationResponse> updateAnnotation(String annotationId, String userId,
      RectangleCoordinates coordinates, String content) throws Exception {
    Annotation annotation = mongoTemplate.findOne(Query.query(Criteria.where("annotationId").is(annotationId)),
        Annotation.class, Constants.ANNOTATION_COLLECTION);

    if (annotation == null) {
      return new ResponseEntity<>(new AnnotationResponse("Annotation does not exist", null), HttpStatus.NOT_FOUND);
    }

    annotation.updateAnnotation(userId, coordinates, content);
    mongoTemplate.save(annotation, Constants.ANNOTATION_COLLECTION);

    return new ResponseEntity<>(new AnnotationResponse("Annotation updated", annotation), HttpStatus.OK);
  }

  public ResponseEntity<AnnotationResponse> voteAnnotation(String annotationId, String userId, Integer vote)
      throws Exception {
    Annotation annotation = mongoTemplate.findOne(Query.query(Criteria.where("annotationId").is(annotationId)),
        Annotation.class, Constants.ANNOTATION_COLLECTION);

    if (annotation == null) {
      return new ResponseEntity<>(new AnnotationResponse("Annotation does not exist", null), HttpStatus.NOT_FOUND);
    }

    annotation.updateVote(userId, vote);
    mongoTemplate.save(annotation, Constants.ANNOTATION_COLLECTION);

    return new ResponseEntity<>(new AnnotationResponse("Annotation vote updated", annotation), HttpStatus.OK);
  }

  public ResponseEntity<AnnotationResponse> deleteAnnotation(String annotationId) {
    Annotation annotation = mongoTemplate.findOne(Query.query(Criteria.where("annotationId").is(annotationId)),
        Annotation.class, Constants.ANNOTATION_COLLECTION);

    if (annotation == null) {
      return new ResponseEntity<>(new AnnotationResponse("Annotation does not exist", null), HttpStatus.NOT_FOUND);
    }

    mongoTemplate.remove(annotation, Constants.ANNOTATION_COLLECTION);

    return new ResponseEntity<>(new AnnotationResponse("Annotation deleted", annotation), HttpStatus.OK);
  }
}
