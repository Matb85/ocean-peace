<script lang="ts">
  import { BackButton, Button, Heading, Aquarium } from "@redinnlabs/system/Elements";
  import { RadioInput, TimeInput, CheckMultiple } from "@redinnlabs/system/Form";

  let type;
  let limit;
</script>

<!-- C O N T E N T -->
<div class="aquarium">
  <Aquarium percent={80} />
</div>

<div class="content">
  <div class="title">
    <Heading tag={3}>Goal - Edit</Heading>
  </div>

  <div class="days">
    <Heading tag={5} className="text-center !font-normal">Select Days</Heading>
    <CheckMultiple
      className="flex-wrap justify-center"
      options={[
        { label: "Mon", isChecked: true },
        { label: "Tue", isChecked: false },
        { label: "Wed", isChecked: false },
        { label: "Thu", isChecked: false },
        { label: "Fri", isChecked: false },
        { label: "Sat", isChecked: false },
        { label: "Sun", isChecked: false },
      ]}
    />
  </div>

  <div class="limit">
    <Heading tag={5} className="text-center !font-normal">Select Limit</Heading>
    <RadioInput bind:chosen={limit} options={[{ value: "Time period" }, { value: "Times opened" }]} />
    <TimeInput />
  </div>

  <div class="type">
    <Heading tag={5} className="text-center !font-normal">Limit Type</Heading>
    <RadioInput bind:chosen={type} options={[{ value: "Soft" }, { value: "Hard" }]} />
  </div>

  <div class="save">
    <a href="/index">
      <Button isFullWidth="true">save</Button>
    </a>
  </div>
</div>

<div class="back">
  <a href="/goaledit">
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
  .save {
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
  .content .days {
    @apply grid
        grid-cols-1
        items-center
        place-items-center
        select-none
        w-11/12
        gap-3;
  }
  .content .limit {
    @apply grid
        grid-cols-1
        items-center
        place-items-center
        select-none
        w-11/12
        gap-5;
  }
  .content .type {
    @apply grid
        grid-cols-1
        items-center
        place-items-center
        select-none
        w-11/12
        gap-3;
  }
</style>
