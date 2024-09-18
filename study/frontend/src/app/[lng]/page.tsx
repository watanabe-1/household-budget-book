"use server";
import { logout } from "@/actions/logout";
import { auth } from "@/auth";
import BodyHeader from "@/components/layout/BodyHeader";

const Home = async () => {
  const session = await auth();

  return (
    <div>
      <BodyHeader title="HOME" />
      <main>
        <div> homeです</div>
        <div>{JSON.stringify(session)}</div>
        <form
          action={async () => {
            "use server";

            await logout();
          }}
        >
          <button type="submit">Sign Out</button>
        </form>
      </main>
    </div>
  );
};

export default Home;
