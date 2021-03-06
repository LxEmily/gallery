import React, { useState, useEffect } from "react";
import styled from "styled-components";

const BoundingBoxContainer = styled.div.attrs({
  className: ``,
})`
  position: absolute;
  display: flex;
`;

const BoundingBox = styled.div.attrs({
  className: ``,
})`
  position: absolute;
  opacity: 0.5;
  display: flex;
  flex-wrap: wrap;
  justify-content: center;

  box-shadow: inset 0 0 0 1.2px
    ${(props) =>
      props.isAddingAnnotation
        ? "#b3b3b3"
        : props.isInView
        ? "#149df2"
        : "#b3b3b3"};

  ${(props) =>
    !props.isAddingAnnotation
      ? `
    cursor: pointer;

    :hover {
    opacity: 1;
  }`
      : `pointer-events: none;`}
`;

const Boxes = ({
  annotations,
  annotationToViewInParent,
  setAnnotationToViewInParent,
  isAddingAnnotation,
  imgWidth,
  imgHeight,
  calculateTopStyle,
  calculateLeftStyle,
  calculateWidthStyle,
  calculateHeightStyle,
}) => {
  const [annotationIdToView, setAnnotationIdToView] = useState(
    annotationToViewInParent ? annotationToViewInParent.annotationId : ""
  );

  useEffect(() => {
    setAnnotationIdToView(
      annotationToViewInParent ? annotationToViewInParent.annotationId : ""
    );
  }, [annotationToViewInParent]);

  function preventDragHandler(e) {
    e.preventDefault();
  }

  function handleBoxOnClick(e, theAnnotation) {
    if (!isAddingAnnotation) {
      setAnnotationIdToView(theAnnotation.annotationId);
      setAnnotationToViewInParent(theAnnotation);
      // console.log(`Clicked Annotation: ${annotation.annotationId}`);
    }
  }

  return (
    <BoundingBoxContainer>
      {annotations.map((annotation, index) => {
        const { x1, y1, x2, y2 } = annotation.rectangleCoordinates;
        return typeof annotation !== undefined ? (
          <BoundingBox
            key={annotation.annotationId}
            style={{
              top: calculateTopStyle(y1, y2, imgWidth, imgHeight),
              left: calculateLeftStyle(x1, x2, imgWidth, imgHeight),
              width: calculateWidthStyle(x1, x2, imgWidth),
              height: calculateHeightStyle(y1, y2, imgHeight),
              zIndex: index,
              // zIndex: 99999 - index,
            }}
            onClick={(e) => handleBoxOnClick(e, annotation)}
            onDragStart={preventDragHandler}
            isAddingAnnotation={isAddingAnnotation}
            isInView={annotationIdToView === annotation.annotationId}
          ></BoundingBox>
        ) : (
          <div key={annotation.annotationId}></div>
        );
      })}
    </BoundingBoxContainer>
  );
};

export default Boxes;

export { BoundingBoxContainer };
