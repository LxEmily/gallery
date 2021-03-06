import React, { useEffect, useState } from "react";
import styled from "styled-components";
import { ReactComponent as Next } from "../../assets/svgs/next.svg";
import content from "../../config/content.json";
import { GetUsername } from "../Common/GetUsername.js";
import { GetAnnotationNum } from "../Common/GetAnnotationNum.js";
import {
  CarouselProvider,
  Slider,
  Slide,
  ButtonBack,
  ButtonNext,
  DotGroup,
  Image,
} from "pure-react-carousel";
import routes from "../../config/routes";

const Container = styled.div.attrs({
  className: `center mt5 mb5`,
})``;

const Title = styled.h2.attrs({
  className: `mt2 mb5 avenir fw6 f2 dark-gray`,
})``;

const CenteredSlider = styled(Slider).attrs({
  className: `center`,
})`
  .carousel__slider-tray {
    transition: 0.2s ease-in-out;
    padding: 0;
    margin: 0;
  }
`;

const CustomSlide = styled(Slide).attrs({ className: `` })`
  list-style-type: none;
`;

const ImageHoverCover = styled.div.attrs({
  className: `child center tc bg-black-40 white v-mid flex flex-column items-center justify-center pointer`,
})`
  position: absolute;
  top: 0;
  bottom: 0;
  left: 0;
  right: 0;
`;

const ImageHoverTitle = styled.h4.attrs({ className: `overflow-hidden` })`
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
`;
const ImageHoverDesc = styled.p.attrs({
  className: `ph4 ph2-m overflow-hidden`,
})`
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
`;
const ImageHoverUser = styled.p.attrs({ className: `i` })``;
const ImageHoverNumberOfViews = styled.p.attrs({ className: `` })``;
const ImageHoverNumberOfAnnotations = styled.p.attrs({ className: `` })``;

function ImageHover(props) {
  const { title, description, username, totalViews, annotationNum } = props;
  return (
    <ImageHoverCover>
      <ImageHoverTitle>{title}</ImageHoverTitle>
      <ImageHoverDesc>{description}</ImageHoverDesc>
      <ImageHoverUser>by {username}</ImageHoverUser>
      <ImageHoverNumberOfViews>{totalViews} views</ImageHoverNumberOfViews>
      <ImageHoverNumberOfAnnotations>
        {annotationNum} annotations
      </ImageHoverNumberOfAnnotations>
    </ImageHoverCover>
  );
}

const RoundDotGroup = styled(DotGroup).attrs({
  className: `tc ma0 pa0`,
})`
  .carousel__dot {
    border: none;
    background-color: gray;
    margin: 1rem;
    width: 1rem;
    height: 1rem;
    border-radius: 50%;
  }
  .carousel__dot:hover {
    background-color: #505050;
    transition: 0.15s ease-in;
  }
  .carousel__dot--selected {
    background-color: #505050;
    transition: 0.15s ease-in;
  }
`;

const CenteredButtonBack = styled(ButtonBack).attrs({
  className: `center bn bg-transparent b--transparent pa0 ma0`,
})`
  position: absolute;
  top: 50%;
  left: 0;
  transform: translateY(-50%);
  fill: gray;
`;

const CenteredButtonNext = styled(ButtonNext).attrs({
  className: `center bn bg-transparent b--transparent pa0 ma0`,
})`
  position: absolute;
  top: 50%;
  right: 0;
  transform: translateY(-50%);
  fill: gray;
`;

const NextIcon = styled(Next).attrs({
  className: ``,
})`
  height: 1.6rem;
  object-fit: contain;
`;

const BackIcon = styled(Next).attrs({
  className: ``,
})`
  height: 1.6rem;
  object-fit: contain;
  transform: rotate(180deg);
`;

/**
 * Images as an array
 * array index is required
 * for each image: source, title, desc, user, viewNum, annotationNum is required
 * @param {*} props
 */
const { recent, get_image } = content.service_endpoints.image;
function ImageCarousel(props) {
  const { setRoute, setRouteData } = props;
  const [isFetching, setIsFetching] = useState(false);
  const [imagesData, setImagesData] = useState([]);

  useEffect(() => {
    if (!isFetching && imagesData.length === 0) {
      setIsFetching(true);
      fetch(`${recent}/5`)
        .then((resp) => resp.json())
        .then((res) => {
          setImagesData(res.images);
          // console.log(res);
        });
    }
  }, [isFetching, imagesData]);

  return (
    <Container>
      <Title>Recent Images</Title>
      <CarouselProvider
        totalSlides={5}
        currentSlide={2}
        isIntrinsicHeight
        hasMasterSpinner
        lockOnWindowScroll
        infinite
        className="overflow-x-hidden ma0"
      >
        <CenteredSlider>
          {imagesData.map((image, index) => (
            <CustomSlide
              index={index}
              classNameHidden="o-50"
              classNameVisible="o-100 grow"
              className="center flex flex-row items-center "
              innerClassName="center v-mid "
              key={index}
            >
              <Image
                src={`${get_image}/${image.id}`}
                tag="div"
                alt={image.title}
                className="hide-child relative contain bg-center"
                style={{ width: "45vw", height: "45vh" }}
                onClick={() => {
                  setRouteData({
                    imageId: image.id,
                  });
                  setRoute(routes.view_image);
                  console.log("CLICKED IMAGE");
                }}
              >
                <ImageHover
                  title={image.title}
                  description={`${
                    image.description
                    // }
                    // .substring(
                    //   0,
                    //   common.truncateAtLength
                    // )}${
                    //   image.description.length > common.truncateAtLength
                    //     ? "..."
                    //     : ""
                  }`}
                  username={<GetUsername userId={image.userId} />}
                  totalViews={image.totalViews}
                  annotationNum={<GetAnnotationNum imageId={image.id} />}
                ></ImageHover>
              </Image>
            </CustomSlide>
          ))}
        </CenteredSlider>
        <CenteredButtonBack>
          <BackIcon />
        </CenteredButtonBack>
        <CenteredButtonNext>
          <NextIcon />
        </CenteredButtonNext>
        <RoundDotGroup />
      </CarouselProvider>
    </Container>
  );
}

export default ImageCarousel;
