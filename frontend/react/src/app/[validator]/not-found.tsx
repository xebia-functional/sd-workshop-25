import { Button } from "@/components/ui/button";
import { ArrowLeft, X } from "lucide-react";
import Link from "next/link";

export default function NotFound() {
  return (
    <div className="flex flex-col gap-4 items-center justify-center h-full">
      <X size={36} className="text-red-600 bg-red-100 rounded-full" />
      <h1 className="text-lg font-bold text-center">Page Not Found</h1>
      <p>Check the URL and use a valid route</p>
      <Button asChild variant="outline" className="w-full h-12 sm:w-auto">
        <Link href={"/"}>
          <ArrowLeft />
          Return to home page
        </Link>
      </Button>
    </div>
  );
}
