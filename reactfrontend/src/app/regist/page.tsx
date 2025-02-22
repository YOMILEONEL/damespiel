import Image from "next/image";
import NavBar from "../components/navBar";

export default function Login() {
  return (
    <div className="page-container">
      <NavBar />

      <div className="main-container">
        <div className="welcome-box">
          <h1 className="title">Willkommen</h1>
          <Image src="https://bigthink.com/wp-content/uploads/2014/10/origin-55.jpg?resize=854" alt="Logo" width={50} height={50} />
          <p className="subtitle">DAMESPIEL</p>
        </div>

        <h2 className="login-title">REGISTRATION</h2>
        <form className="login-form">
          
        </form>
      </div>
      <input type="text" placeholder="Name" className="login-input" />
          <input type="password" placeholder="Passwort" className="login-input" />
          <button type="submit" className="login-button">registration</button>
    </div>
  );
}
