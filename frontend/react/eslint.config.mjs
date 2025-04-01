import { dirname } from "path";
import { fileURLToPath } from "url";
import { FlatCompat } from "@eslint/eslintrc";

const __filename = fileURLToPath(import.meta.url);
const __dirname = dirname(__filename);

const compat = new FlatCompat({
  baseDirectory: __dirname,
});

const eslintConfig = [
  ...compat.extends("next/core-web-vitals", "next/typescript"),

  // Ignore non-relevant files globally
  {
    ignores: [
      "**/*.css",
      "**/*.json",
      "**/*.yaml",
      "**/*.lock",
      "**/*.config.js",
      ".next/",
      "node_modules/",
      "dist/",
      "next-env.d.ts/",
      ".env*",
      ".local",
      "public/*",
      ".vscode",
    ],
  },

  // Override rule for Tailwind config to allow require()
  {
    files: ["tailwind.config.js"],
    rules: {
      "@typescript-eslint/no-require-imports": "off",
    },
  },
];

export default eslintConfig;
