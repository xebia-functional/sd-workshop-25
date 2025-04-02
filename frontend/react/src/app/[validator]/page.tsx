import { Button } from "@/components/ui/button";
import data from "@/lib/data";
import Link from "next/link";

export default async function Validator({
  params,
}: {
  params: { validator: string };
}) {
  const { validator } = await params;
  const config = data.find((item) => item.name === validator);
  if (!config) {
    console.error(`Validator ${validator} not found`);
    return;
  }
  return (
    <>
      <h1 className="text-4xl sm:text-5xl font-bold text-center">
        {`${config.title} validator`}
      </h1>
      <Button
        asChild
        variant="outline"
        className="w-full h-12 cursor-pointer sm:w-auto"
      >
        <Link href={"/"}>Return to home page</Link>
      </Button>
    </>
  );
}
