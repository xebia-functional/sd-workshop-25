"use client";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { useState } from "react";
import { Send, CheckCircle, XCircle, X, ArrowLeft } from "lucide-react";
import Link from "next/link";

type Props = {
  endpoint: string;
};
type Status = {
  type: "success" | "error";
  message: string;
};
type ValidationStatus = Status | null;

const ValidationForm = ({ endpoint }: Props) => {
  const [value, setValue] = useState("");
  const [status, setStatus] = useState<ValidationStatus>(null);
  const isError = (error: unknown): error is Error => error instanceof Error;

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    try {
      const response = await fetch(`http://localhost:8080${endpoint}ZZZ`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: value,
      });

      const data = await response.text();
      const type = response.ok ? "success" : "error";
      setStatus({ type, message: data });
    } catch (error: unknown) {
      const message = isError(error)
        ? error.message
        : "An unknown error occurred.";
      setStatus({ type: "error", message });
    }
  };

  const clearForm = () => {
    setValue("");
    setStatus(null);
  };

  return (
    <div className="w-full max-w-sm">
      <form onSubmit={handleSubmit} className="flex items-center gap-2">
        <Input
          type="text"
          placeholder="Enter your ID here..."
          value={value}
          onChange={(e) => setValue(e.target.value)}
          className="w-full"
        />
        <Button
          type="submit"
          className="flex gap-1 items-center"
          disabled={!value}
        >
          <Send size={16} />
          Submit
        </Button>
      </form>
      <div className="mt-3 min-h-[200px] flex items-center ">
        {status && (
          <div
            className={`flex items-center gap-2 p-2 rounded-md text-sm p-4 ${
              status.type === "success" ? "bg-green-100" : "bg-red-100"
            }`}
          >
            {status.type === "success" ? (
              <CheckCircle size={20} className="text-green-600" />
            ) : (
              <XCircle size={20} className="text-red-600" />
            )}
            <span
              className={
                status.type === "success" ? "text-green-700" : "text-red-700"
              }
            >
              {status.message}
            </span>
          </div>
        )}
      </div>
      <div className="flex gap-2 justify-between mt-4">
        <Button asChild variant="outline" className="h-12 sm:w-auto">
          <Link href={"/"}>
            <ArrowLeft />
            Return to home page
          </Link>
        </Button>
        {(value || !!status) && (
          <Button
            variant="ghost"
            onClick={clearForm}
            className="h-12 sm:w-auto"
          >
            <X size={16} />
            Clear form
          </Button>
        )}
      </div>
    </div>
  );
};

export default ValidationForm;
