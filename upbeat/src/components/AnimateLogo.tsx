import { BackgroundCloud } from "./animate/BackgroundCloud";
import { Pupil } from "./animate/Pupil";
import { ForegroundCloud } from "./animate/ForegroundCloud";
import { PartsWrapper } from "./animate/PartsWrapper";
import { Tower } from "./animate/Tower";
import { Sclera } from "./animate/Sclera";

export default function AnimateLogo() {
  return (
    <main className="AnimateLogo mx-10 my-10">
      <PartsWrapper>
        <BackgroundCloud />
        <Sclera />
        <Pupil />
        <Tower />
        <ForegroundCloud />
      </PartsWrapper>
    </main>
  );
}
