"use client";
import { Button } from "@/components/ui/button";
import Link from "next/link";
import data from "./data";

const HomeGrid: React.FC = () => {
  return (
    <div className="grid grid-cols-1 [@media(min-width:450px)]:grid-cols-3 gap-8 sm:min-w-[500px]">
      {data.map((item) => (
        <Button
          key={item.href}
          asChild
          variant="outline"
          className="w-full h-12 cursor-pointer sm:w-auto"
        >
          <Link href={"/raw-class"}>{item.title}</Link>
        </Button>
      ))}
    </div>
  );
};

export default HomeGrid;
