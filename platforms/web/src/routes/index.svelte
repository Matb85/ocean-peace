<script lang="ts">
  
  import "@redinnlabs/system/utils/base.css";
  import { Button, Icon, Heading, Aquarium } from "@redinnlabs/system/Elements";
  import { Goal } from "@redinnlabs/system/Units";
  import { mdiPencil } from '@mdi/js';
</script>

<main>
  <!-- content -->
  <div class="content">
    <div class="w-full h-96 block">
      <Aquarium percent={80}></Aquarium>
    </div>
    <div class="startFocus_btn" >
      <Button>Start a focus session</Button>
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
    <a href="/focus">focus</a>
  </div>

</main>

<style lang="postcss">
  :root {
    font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, Oxygen, Ubuntu, Cantarell, "Open Sans",
      "Helvetica Neue", sans-serif;
  }
  .content {
    display: flex;
    flex-direction: column;
    align-items: center;
    place-content: center;
    pointer-events: all;
    user-select: none;
    margin-bottom: 60px;
    gap: 20px;
  }

  .startFocus_btn {

  }

  .goals_title {
    
  }

  .goals {
    @apply place-content-center place-items-center
      grid grid-cols-1
      gap-5;
      
  }
  
  
</style>
