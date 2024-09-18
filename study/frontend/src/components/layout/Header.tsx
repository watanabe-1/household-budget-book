import { auth } from "@/auth";
import React from "react";

type HeaderProps = {};

/**
 *
 * @returns Header
 */
const Header: React.FC<HeaderProps> = async () => {
  const session = await auth();

  const userName = session?.user?.name ?? "ゲスト";
  const greeting = `${userName}さん`;

  return (
    <header className="navbar navbar-dark sticky-top bg-black text-white flex-md-nowrap p-0 shadow">
      <div className="flex items-center justify-between">
        <ul className="flex list-none">
          <li className="list-none">
            <a className="navbar-brand">study kakeibo</a>
          </li>
          <li className="list-none">
            {/* <div th:replace="~{common/sidebar :: sidebar_btn_fragment}"></div> */}
          </li>
        </ul>
        <span className="navbar-text px-3">{greeting}</span>
      </div>
    </header>
  );
};

export default Header;
