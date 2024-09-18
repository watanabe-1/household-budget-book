/** @type {import('next').NextConfig} */
const nextConfig = {
  i18n: {
    locales: ["en", "ja", "es"], // サポートする言語のリスト
    defaultLocale: "ja", // デフォルトの言語
  },
};

export default nextConfig;
