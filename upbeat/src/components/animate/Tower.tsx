import React from "react";
import styled from "styled-components";
import { motion } from "framer-motion";

const Svg = styled(motion.svg).attrs({
  viewBox: "0 0 68 79",
  xmlns: "http://www.w3.org/2000/svg",
})`
  bottom: 30%;
`;

interface TowerProps {
  hovered?: boolean;
}

export const Tower = ({ hovered = false }: TowerProps) => (
  <Svg animate={{ y: hovered ? "5%" : "0%" }} width="100%" height="100%">
    <path
      fillRule="evenodd"
      clipRule="evenodd"
      d="M67.847 78.2857H0.150398L3.388 12.913L0.967899 0.506214L15.8348 0.497559V12.913H27.2368V0.497559H40.7628V12.913H52.1649V0.497559L67.03 0.506214L64.6116 12.913L67.847 78.2857ZM12.0669 31.8679C12.0679 31.8185 12.0688 31.7693 12.0695 31.7202H55.926C55.9303 31.8738 55.9325 32.0253 55.9325 32.1789C55.9325 44.298 46.1114 54.1234 33.9977 54.1234C21.8841 54.1234 12.063 44.298 12.063 32.1789C12.063 32.075 12.065 31.9712 12.0669 31.8679Z"
      fill="url(#paint0_linear)"
    />
    <defs>
      <linearGradient
        id="paint0_linear"
        x1="34.1184"
        y1="84.4655"
        x2="33.1594"
        y2="2.91669"
        gradientUnits="userSpaceOnUse"
      >
        <stop stopColor="#F05145" />
        <stop offset="0.24" stopColor="#F17B6E" />
        <stop offset="0.48" stopColor="#F69674" />
        <stop offset="0.78" stopColor="#F8A575" />
        <stop offset="1" stopColor="#F9AB77" />
      </linearGradient>
    </defs>
  </Svg>
);

export default Tower;
