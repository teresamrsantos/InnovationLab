import React from "react";
import { motion } from "framer-motion";

const containerStyle = {
  position: "relative",
  width: "12rem",
  height: "12rem",
  boxSizing: "border-box"
};

const circleStyle = {
  display: "block",
  width: "12rem",
  height: "12rem",
  border: "2rem solid #e9e9e9",
  borderTop: "2rem solid #EF3D4E",
  borderRadius: "50%",
  position: "absolute",
  boxSizing: "border-box",
  top: 0,
  left: 0
};

const spinTransition = {
  loop: Infinity,
  ease: "linear",
  duration: 1
};

export default function CircleLoader() {
  return (
    <div style={containerStyle}>
      <motion.span style={circleStyle} animate={{ rotate: 360 }} transition={spinTransition}/>
    </div>
  );
}