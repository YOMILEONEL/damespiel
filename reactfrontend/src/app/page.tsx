"use client"; 

import Image from "next/image";
import NavBar from "./components/navBar";
import {useRouter} from "next/navigation";

export default function Home() {

  const router = useRouter();

  const loginHandler = () => {
    router.push("/login");
  };
  const registHandler = () => {
    router.push("/regist");
  };
   
  return (
    <div className="page-container">
      <NavBar />
      <div className="main-container">
        <div className="welcome-box">
          <h1 className="title">Willkommen</h1>
          <Image src="https://bigthink.com/wp-content/uploads/2014/10/origin-55.jpg?resize=854" alt="Logo" width={50} height={50} />
          <p className="subtitle">DAMESPIEL</p>
        </div>
      </div>
      <div className="button-area">
      <button className="action-button red" onClick={loginHandler}>EINLOGEN</button>
      <button className="action-button blue" onClick={registHandler}>REGISTRIEREN</button>
        </div>
    </div>
  );
}
