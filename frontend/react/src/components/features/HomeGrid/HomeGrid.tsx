"use client";
import { Button } from "@/components/ui/button";

const HomeGrid: React.FC = () => {
  return (
    <div className="flex gap-4 items-center flex-col sm:flex-row">
      <Button onClick={() => console.log("Clicked!!")}>Click me</Button>
    </div>
  );
};

export default HomeGrid;
