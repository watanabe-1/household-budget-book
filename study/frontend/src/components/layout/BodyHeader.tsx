import React from "react";

type BodyHeaderProps = {
  /** bodyのタイトル */
  title: string;
};

/**
 *
 * @returns Header
 */
const BodyHeader: React.FC<BodyHeaderProps> = ({ title }) => {
  return (
    <div className="flex justify-between flex-wrap md:flex-nowrap items-center pt-3 pb-2 mb-3 border-b border-gray-300">
      <h1 className="text-lg md:text-xl font-bold">{title}</h1>
      <div className="ml-auto"></div>
    </div>
  );
};

export default BodyHeader;
