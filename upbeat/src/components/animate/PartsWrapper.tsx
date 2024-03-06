import React, {
  ReactNode,
  useState,
  isValidElement,
  useEffect,
  useRef,
} from "react";
import styled from "styled-components";
import { motion } from "framer-motion";
import { throttle } from "throttle-debounce";

const Wrapper = styled(motion.div)`
  width: 100%;
  position: relative;
  cursor: pointer;

  > * {
    position: absolute;
    bottom: 0;
    left: 0;

    &:first-child {
      position: unset;
    }
  }
`;

const commonTransition = {
  type: "spring",
  stiffness: 140,
  damping: 10,
};

const getRandomTapRotate = () => `${Math.random() >= 0.5 ? "-" : ""}5deg`;
const getValidMovementValue = (
  clientPoint: number,
  { start, end }: { start: number; end: number }
) => {
  const shrinkValue = 5;
  let movementValue = 0;

  if (clientPoint < start) {
    movementValue = -start + clientPoint;
  } else if (clientPoint > end) {
    movementValue = clientPoint - end;
  }

  movementValue = movementValue / shrinkValue;

  return movementValue;
};

interface PartsWrapperProps {
  children: ReactNode;
}

export const PartsWrapper = ({ children }: PartsWrapperProps) => {
  const wrapperRef = useRef<HTMLDivElement>(null);
  const [isHovered, setIsHovered] = useState(false);
  const [position, setPosition] = useState({ x: 0, y: 0 });
  const [rotate, setRotate] = useState(getRandomTapRotate());

  useEffect(() => {
    const handleMouseMove = throttle(
      100,
      ({ clientX, clientY }: MouseEvent) => {
        if (wrapperRef.current?.getBoundingClientRect() == null) return;

        const { height, width, x, y } =
          wrapperRef.current?.getBoundingClientRect();

        setPosition({
          x: getValidMovementValue(clientX, { start: x, end: x + width }),
          y: getValidMovementValue(clientY, { start: y, end: y + height }),
        });
      }
    );

    window.addEventListener("mousemove", handleMouseMove);
    return () => window.removeEventListener("mousemove", handleMouseMove);
  }, []);

  return (
    <Wrapper
      animate={position}
      transition={{
        type: "spring",
        stiffness: 80,
        damping: 20,
      }}
      onHoverEnd={() => setIsHovered(false)}
      onHoverStart={() => setIsHovered(true)}
      onTap={() => setRotate(getRandomTapRotate())}
      ref={wrapperRef}
      whileHover={{ scale: 0.9, transition: commonTransition }}
      whileTap={{ rotate: rotate, transition: commonTransition }}
    >
      {React.Children.map(children, (child) =>
        isValidElement(child)
          ? React.cloneElement(child, { hovered: isHovered })
          : child
      )}
    </Wrapper>
  );
};

export default PartsWrapper;
