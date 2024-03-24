import React, { useState, useEffect } from "react";

interface CountdownProps {
  initialMinutes: number;
  initialSeconds: number;
}

const TimerCom: React.FC<CountdownProps> = ({
  initialMinutes,
  initialSeconds,
}) => {
  const [time, setTime] = useState({
    minutes: initialMinutes,
    seconds: initialSeconds,
  });

  useEffect(() => {
    const storedMinutes = localStorage.getItem("countdownMinutes");
    const storedSeconds = localStorage.getItem("countdownSeconds");

    let storedInitialMinutes = storedMinutes
      ? parseInt(storedMinutes, 10)
      : initialMinutes;
    let storedInitialSeconds = storedSeconds
      ? parseInt(storedSeconds, 10)
      : initialSeconds;

    if (storedInitialMinutes < 0) storedInitialMinutes = 0;
    if (storedInitialSeconds < 0) storedInitialSeconds = 0;

    setTime({ minutes: storedInitialMinutes, seconds: storedInitialSeconds });

    const timer = setInterval(() => {
      setTime((prevTime) => {
        if (prevTime.seconds === 0) {
          if (prevTime.minutes === 0) {
            clearInterval(timer);
            return prevTime;
          } else {
            return { minutes: prevTime.minutes - 1, seconds: 59 };
          }
        } else {
          return { minutes: prevTime.minutes, seconds: prevTime.seconds - 1 };
        }
      });
    }, 1000);

    return () => clearInterval(timer);
  }, [initialMinutes, initialSeconds]);

  useEffect(() => {
    localStorage.setItem(
      "countdownMinutes",
      Math.max(0, time.minutes).toString()
    );
    localStorage.setItem(
      "countdownSeconds",
      Math.max(0, time.seconds).toString()
    );
  }, [time.minutes, time.seconds]);

  return (
    <div className="bg-blue-900 p-4 px-6 rounded-2xl shadow-xl">
      <p className="font-semibold text-white text-3xl">
        {`${time.minutes.toString().padStart(2, "0")}:${time.seconds
          .toString()
          .padStart(2, "0")}`}
      </p>
    </div>
  );
};

export default TimerCom;
export type { CountdownProps };
