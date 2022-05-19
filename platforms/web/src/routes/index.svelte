<script lang="ts">
  import logo from "../assets/logo.svg";
  import photoNull from "../assets/null_photo.svg";
  import background from "../assets/background.svg";
  import Button from "@redinnlabs/system/Elements/Button/Button.svelte";
  import { ToDo, Goal } from "@redinnlabs/system/Units";
</script>

<main>
  <!-- Background -->
  <div class="bg">
    <img src={background} alt="Svelte Logo" />
  </div>

  <!-- Navigation Bar -->
  <div class="nav">
    <div class="profile">
      <img class="pic" src={photoNull} alt="Svelte Logo" />
      <p class="name">John Doe</p>
      <div class="overscroll-none rounded-full text-primary1">hii</div>

      <Button>test</Button>
      <Goal title="test" />
    </div>
    <div class="logo">
      <img class="pic" src={logo} alt="Svelte Logo" />
      <p class="text">OceanPeace</p>
    </div>
  </div>
</main>

<style lang="postcss">
  :root {
    font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, Oxygen, Ubuntu, Cantarell, "Open Sans",
      "Helvetica Neue", sans-serif;
  }
  /* Background */
  .bg {
    height: 100vh;
    width: 100vw;
    position: absolute;
    left: 0%;
    top: 0%;
    overflow: hidden;
  }
  .bg img {
    height: max-content;
    width: max-content;
  }

  /* Navigation Bar */
  .nav {
    height: 100vh;
    width: 308px;
    background-color: #ffffff;
    border-radius: 0px 32px 32px 0px;
    position: absolute;
    top: 0%;
    left: 0%;
    display: flex;
    flex-direction: column;
    align-items: center;
  }

  .nav .profile {
    width: 171px;
    position: absolute;
    display: flex;
    flex-direction: column;
    align-items: center;
    top: 98px;
  }
  .nav .profile .pic {
    position: relative;
    height: 171px;
    width: 171px;
    filter: drop-shadow(0px 2px 18px rgba(0, 0, 0, 0.25));
    border-radius: 24px;
  }
  .nav .profile .name {
    width: 171px;
    font-family: "Poppins";
    font-style: normal;
    font-weight: 400;
    font-size: 21px;
    line-height: 120%;
    text-align: center;
  }

  .nav .logo {
    height: 35px;
    width: 210px;
    top: 35px;
    position: absolute;
    display: flex;
    flex-direction: row;
  }
  .nav .logo .pic {
    height: 35px;
    width: 42px;
  }
  .nav .logo .text {
    height: 35px;
    width: 161px;
    font-family: "Lato";
    font-style: normal;
    font-weight: 700;
    font-size: 25px;
    line-height: 35px;
    margin: 0px 7px;
  }
</style>
