<script lang="ts">
  
  import "@redinnlabs/system/utils/base.css";
  import { Button, Icon, Heading, Aquarium } from "@redinnlabs/system/Elements";
  import { Goal } from "@redinnlabs/system/Units";
  import { mdiPencil } from '@mdi/js';
  
  export let curScreenTime: number = 65;
  export let maxScreenTime: number = 270;

  function startFocus () {
    location.href='/focus';
  };
</script>

<main>
  <!-- content -->
  <div class="content">

    <div class="w-full h-96 block ">
      <Aquarium percent={(maxScreenTime - curScreenTime)/maxScreenTime < 0 ? 0 : (maxScreenTime - curScreenTime)/maxScreenTime * 100}></Aquarium>
      
    </div>
    <div class="screentime">
      <Heading tag={5} className="text-white">Your Screen time</Heading>
      <Heading tag={2} className="text-white">{curScreenTime < 59 ? '' : Math.floor(curScreenTime/ 60) + "h"} {curScreenTime%60 == 0 ? '' : curScreenTime % 60 + 'min'}</Heading>
      <Heading tag={4} className="text-white">{Math.floor((maxScreenTime - curScreenTime)/ 60) + 'h'} {(maxScreenTime - curScreenTime)% 60 +'min'} left</Heading>
    </div>
    <div class="startFocus_btn" >
      <Button on:click={startFocus}>Start a focus session</Button>
    </div>

    <div class="goals_title">
      <Heading tag={6}>Your Goals</Heading>
    </div>
    <div class="goals">
      {#each Array(4) as _, i}
        <Goal title={"Some goal here"} info={"something left"} className="Goal"></Goal>
      {/each}
        <Button secondary=true >Add Goal</Button>
    </div>

  </div>

</main>

<style lang="postcss">
  :root {
    font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, Oxygen, Ubuntu, Cantarell, "Open Sans",
      "Helvetica Neue", sans-serif;
  }

  .screentime{
    @apply absolute
      top-44
      grid
      grid-cols-1
      place-items-center;
  }
  .content {
    pointer-events: all;
    @apply 
        flex
        flex-col
        gap-5
        items-center
        place-content-center
        select-none
        mb-14;
  }

  .startFocus_btn {

  }

  .goals_title {
    
  }

  .goals {
    @apply place-content-center place-items-center
      grid grid-cols-1
      w-11/12
      gap-5;
      
  }
  
  
</style>
