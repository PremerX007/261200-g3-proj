import React from "react";
import styled from "styled-components";
import { motion } from "framer-motion";

const Svg = styled(motion.svg).attrs({
  viewBox: "0 0 44 24",
  xmlns: "http://www.w3.org/2000/svg",
})`
  fill: none;
  bottom: 58%;
  left: 32.5%;

  > * {
    fill: white;
  }
`;

interface ScleraProps {
  hovered?: boolean;
}

export const Sclera = ({ hovered = false }: ScleraProps) => (
  <Svg animate={{ y: hovered ? "15%" : "0%" }} height="35%" width="35%">
    <path
      d="M0.0694763 0.720215C0.0673135 0.871676 0.0629883 1.0253 0.0629883 1.17892C0.0629883 13.298 9.88409 23.1234 21.9977 23.1234C34.1114 23.1234 43.9325 13.298 43.9325 1.17892C43.9325 1.0253 43.9303 0.873839 43.926 0.720215H0.0694763Z"
      fill="white"
    />
  </Svg>
);

export default Sclera;
