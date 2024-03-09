import React from "react";

function NotReadyIcon() {
  return (
    <svg xmlns="http://www.w3.org/2000/svg" width="41" height="41">
      <circle
        r="35"
        fill="#FFF"
        stroke="#000"
        strokeWidth="0"
        transform="matrix(0.4 0 0 0.4 20 20)"
        vectorEffect="non-scaling-stroke"
      ></circle>
      <path
        fill="#FF4141"
        fillRule="evenodd"
        d="M0-25.31c13.98 0 25.31 11.33 25.31 25.31 0 13.98-11.33 25.31-25.31 25.31-13.98 0-25.31-11.33-25.31-25.31 0-13.98 11.33-25.31 25.31-25.31zM4.95-9.18a2.88 2.88 0 014.11-.01c1.14 1.14 1.14 3 .01 4.15L4.1 0l4.97 5.05c1.12 1.14 1.11 2.99-.03 4.13-1.14 1.14-2.98 1.14-4.1-.01L0 4.16l-4.95 5.02a2.88 2.88 0 01-4.11.01c-1.14-1.14-1.14-3-.01-4.15L-4.1 0l-4.98-5.05c-1.12-1.14-1.11-2.99.03-4.13 1.14-1.14 2.98-1.14 4.1.01l4.94 5.01 4.96-5.02z"
        transform="matrix(0.75 0 0 0.75 20 20)"
        vectorEffect="non-scaling-stroke"
      ></path>
    </svg>
  );
}

export default NotReadyIcon;
