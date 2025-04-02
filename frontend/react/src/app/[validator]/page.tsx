import { ValidationForm } from "@/components/features/ValidationForm";
import data from "@/lib/data";

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
      <ValidationForm />
    </>
  );
}
