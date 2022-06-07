<script lang="ts">
  import { BackButton, Button, Heading, Aquarium } from "@redinnlabs/system/Elements";
  import { SearchBar } from "@redinnlabs/system/Units";
  import { TextInput } from "@redinnlabs/system/Form";
</script>

<!-- C O N T E N T -->
<div class="aquarium">
  <Aquarium percent={80} />
</div>

<div class="content">
  <div class="title">
    <Heading tag={3}>Goal - Edit</Heading>
  </div>

  <div class="name">
    <Heading tag={5} className="text-center !font-normal">Name</Heading>
    <TextInput placeholder="Name of your goal" />
  </div>

  <div class="apps">
    <Heading tag={5} className="text-center !font-normal">Selected apps</Heading>
    <SearchBar placeholder="Search..." />
  </div>

  <div class="next">
    <a href="/goaledit2">
      <Button isFullWidth="true">next</Button>
    </a>
  </div>
</div>

<div class="back">
  <a href="/goal">
    <BackButton />
  </a>
</div>

<style lang="postcss">
  /* F I X E D   I T E M S */
  .back {
    @apply absolute
        top-5 left-3
        min-h-fit min-w-fit;
  }
  .title {
    @apply absolute
            top-7;
  }
  .next {
    @apply fixed
        bottom-10
        w-11/12;
  }

  /* C O N T E N T   E L E M E N T S */

  .content {
    @apply flex flex-col 
        items-center place-content-center
        select-none
        mb-32 mt-28
        gap-8
        z-10;
    pointer-events: all;
    scrollbar-width: none;
  }
  .content .name {
    @apply grid
        grid-cols-1
        items-center
        place-items-center
        select-none
        w-11/12
        gap-3;
  }
  .content .apps {
    @apply grid
        grid-cols-1
        items-center
        place-items-center
        select-none
        w-11/12
        gap-3;
  }
</style>
