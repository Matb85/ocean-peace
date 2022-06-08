<script>
  import { Heading as H } from "@redinnlabs/system/Elements";
</script>

<div class="fixed bottom-0 w-full bg-white h-12 shadow-md shadow-black flex justify-around items-center">
  <a sveltekit:prefetch href="/profile">
    <H tag={6}>Profile</H>
  </a>
  <a sveltekit:prefetch href="/">
    <H tag={6}>Home</H>
  </a>
  <a sveltekit:prefetch href="/focus">
    <H tag={6}>Focus</H>
  </a>
</div>
