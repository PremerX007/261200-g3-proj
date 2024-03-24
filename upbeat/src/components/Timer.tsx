import React, { useState, useEffect, Dispatch, SetStateAction } from "react";

interface CountdownProps {
  initialMinutes: number;
  initialSeconds: number;
  onReset: () => void;
  isActive: boolean;
  setIsActive: Dispatch<SetStateAction<boolean>>;
}

const TimerCom: React.FC<CountdownProps> = ({
  initialMinutes,
  initialSeconds,
  onReset,
  isActive,
  setIsActive,
}) => {
  const [time, setTime] = useState({
    minutes: initialMinutes,
    seconds: initialSeconds,
  });

  useEffect(() => {
    let interval: NodeJS.Timeout;
    if (isActive) {
      interval = setInterval(() => {
        setTime((prevTime) => {
          if (prevTime.seconds === 0) {
            if (prevTime.minutes === 0) {
              clearInterval(interval);
              setIsActive(false);
              return prevTime;
            } else {
              return { minutes: prevTime.minutes - 1, seconds: 59 };
            }
          } else {
            return { ...prevTime, seconds: prevTime.seconds - 1 };
          }
        });
      }, 1000);
    } else if (!isActive && time.seconds !== 0) {
      console.log(time.minutes);
      console.log(time.seconds);
      clearInterval(interval!);
    }

    return () => clearInterval(interval);
  }, [isActive, time]);

  useEffect(() => {
    localStorage.setItem(
      "countdownMinutes",
      Math.max(0, time.minutes).toString()
    );
    localStorage.setItem(
      "countdownSeconds",
      Math.max(0, time.seconds).toString()
    );
  }, [time]);

  useEffect(() => {
    setTime({ minutes: initialMinutes, seconds: initialSeconds });
  }, [onReset]);

  return (
    <div className="bg-blue-900 p-2 px-6 rounded-2xl shadow-xl animate-bounce">
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
