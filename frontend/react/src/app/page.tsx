import { HomeGrid } from "@/components/features/HomeGrid";

export default function Home() {
  return (
    <div className="flex gap-14 items-center flex-col">
      <h1 className="text-4xl sm:text-5xl font-bold text-center">
        Welcome to ScalaDays ID Validator
      </h1>
      <HomeGrid />
    </div>
  );
}
