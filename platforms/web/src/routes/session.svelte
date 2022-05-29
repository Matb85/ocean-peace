<script lang="ts">
  
    import "@redinnlabs/system/utils/base.css";
    import { BackButton, Button, Icon, Heading, EditButton } from "@redinnlabs/system/Elements";
    import { TimeInput, RadioInput, CheckMultiple } from "@redinnlabs/system/Form";
    import { SoundTrack, ToDo } from "@redinnlabs/system/Units";
    import { CircleChart } from "@redinnlabs/system/Charts";
    import { mdiPencil } from '@mdi/js';


    export let presetName: string = 'Example';
    export let appsCount: number = 16;

</script>

    <!-- C O N T E N T -->
    
    <div class="content">
        <!-- svelte-ignore component-name-lowercase -->
        <div class="title">
            <Heading tag={3}>{presetName} Session</Heading>
        </div>

        <div class="graph">
            <CircleChart className="w-full h-full"/>
            <Heading tag={6} className="text-center">{appsCount} {appsCount > 1 ? "apps" : "app"} available</Heading>
            <Heading tag={6} className="text-center !font-normal">You will get 25 points for this session</Heading>
        </div>

        <div class="cancel">
            <a href="/summary">
                <Button secondary=true isWarning=true>Cancel Session</Button>
            </a>
        </div>
        
        <div class="todo">
            <Heading tag={5} className="text-center">Things to do later</Heading>
            {#each Array(5) as _, i }
                <ToDo title="Example" info="Example info"/>
            {/each}
            <Button>Add a todo</Button>
        </div>
    </div>
    


<style lang="postcss">
    /* F I X E D   I T E M S */
    .back {
        @apply
            absolute
            top-5 left-3
            min-h-fit min-w-fit;
    }
    .edit {
        @apply
            absolute
            top-5 right-3
            min-h-fit min-w-fit;
    }
    .start {
        @apply
            fixed
            bottom-10
            min-h-fit min-w-fit;
    }

    /* C O N T E N T   E L E M E N T S */

    .title {
        @apply
            absolute
            top-7;
    }
    .content {
        @apply 
            flex flex-col 
            items-center place-content-center
            select-none
            mb-16 mt-28
            gap-10
            ;
        pointer-events: all;
        scrollbar-width: none;
    }
    .content .graph {
        @apply
            w-9/12;
    }
    .content .todo{
        @apply 
            grid
            grid-cols-1
            items-center
            place-items-center
            select-none
            w-11/12
            gap-4;
    }
</style>