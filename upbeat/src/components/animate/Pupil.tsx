import React, { useState, useEffect } from "react";
import styled from "styled-components";
import { motion } from "framer-motion";

const Svg = styled(motion.svg).attrs({
  viewBox: "0 0 18 18",
  xmlns: "http://www.w3.org/2000/svg",
})`
  && {
    bottom: 68%;
    left: 40%;
  }
  * {
    fill: #404040;
  }
`;

const initialPositionIdx = 2;
const positions = [
  { x: "-50%", y: 0 },
  { x: 0, y: "-25%" },
  { x: 0, y: 0 },
  { x: 0, y: "40%" },
  { x: "50%", y: 0 },
];

const getRandomPositionIdx = (currentIdx: number): number => {
  const randomIdx = Math.floor(Math.random() * (positions.length + 1));

  return currentIdx !== randomIdx
    ? randomIdx
    : getRandomPositionIdx(currentIdx);
};

interface PupilProps {
  hovered?: boolean;
}

export const Pupil = ({ hovered = false }: PupilProps) => {
  const [positionIdx, setPositionIdx] = useState(initialPositionIdx);

  useEffect(() => {
    const interval = setInterval(
      () => setPositionIdx((idx) => getRandomPositionIdx(idx)),
      2000
    );

    return () => clearInterval(interval);
  }, [hovered]);

  return (
    <Svg
      animate={hovered ? positions[initialPositionIdx] : positions[positionIdx]}
      transition={{
        type: "spring",
        stiffness: 140,
        damping: 20,
      }}
      height="20%"
      width="20%"
    >
      <path d="M8.9999 17.8865C13.8983 17.8865 17.8694 13.9138 17.8694 9.0131C17.8694 4.1124 13.8983 0.139603 8.9999 0.139603C4.1014 0.139603 0.130402 4.1124 0.130402 9.0131C0.130402 13.9138 4.1014 17.8865 8.9999 17.8865Z" />
    </Svg>
  );
};

export default Pupil;
