import React from "react";

type HeaderProps = {
  /** bodyのタイトル */
  title: string;
};

/**
 *
 * @returns Header
 */
const Header: React.FC<HeaderProps> = ({ title }) => {
  return (
    <div className="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom">
      <h1 className="h3">{title}</h1>
      <div className="btn-group ml-auto"></div>
    </div>
  );
};

export default Header;
