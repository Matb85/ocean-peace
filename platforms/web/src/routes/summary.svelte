<script lang="ts">
  import "@redinnlabs/system/utils/base.css";
  import { Button, Heading } from "@redinnlabs/system/Elements";

  const minutes: number = 148;
</script>

<!-- C O N T E N T -->

<div class="content">
  <!-- svelte-ignore component-name-lowercase -->
  <div class="title text-shadow">
    <Heading tag={3} className="text-white">Summary</Heading>
  </div>

  <div class="summary text-shadow">
    <Heading tag={3} className="text-center text-white">
      {minutes < 59 ? "" : Math.floor(minutes / 60) + "h"}
      {minutes % 60 == 0 ? "" : (minutes % 60) + "min"}
    </Heading>
    <Heading tag={6} className="text-center !font-normal text-white">of focus</Heading>
  </div>

  <a href="/" class="notification">
    <Heading tag={5} className="text-center">Notification received</Heading>
    <Button>continue</Button>
  </a>
</div>

<div class="waves">
  <img src="/summary.svg" alt="wave" />
</div>

<style lang="postcss">
  /* F I X E D   I T E M S */

  .text-shadow {
    filter: drop-shadow(2px 2px 4px rgba(0, 0, 0, 0.4));
  }
  /* C O N T E N T   E L E M E N T S */

  .waves {
    @apply absolute
            top-0
            -z-50
            w-full;
  }

  .title {
    @apply absolute
            top-7;
  }
  .content {
    @apply flex flex-col 
            items-center place-content-center
            select-none
            mb-16 mt-28
            gap-10;
    pointer-events: all;
    scrollbar-width: none;
  }
  .content .summary {
    @apply w-10/12;
  }
  .content .notification {
    @apply grid
            grid-cols-1
            items-center
            place-items-center
            select-none
            w-11/12
            gap-4;
  }
</style>
