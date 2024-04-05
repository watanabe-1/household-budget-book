import { logout } from "@/actions/logout";
import { auth } from "@/auth";
import Button from "react-bootstrap/Button";

const Home = async () => {
  const session = await auth();

  return (
    <main>
      <div> homeです</div>
      <div>{JSON.stringify(session)}</div>
      <form
        action={async () => {
          "use server";

          await logout();
        }}
      >
        <Button type="submit">Sign Out</Button>
      </form>
    </main>
  );
};

export default Home;
