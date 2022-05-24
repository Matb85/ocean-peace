<script lang="ts">
  
    import "@redinnlabs/system/utils/base.css";
    import { BackButton, Button, Icon, Heading, EditButton } from "@redinnlabs/system/Elements";
    import { TimeInput, RadioInput, CheckMultiple } from "@redinnlabs/system/Form";
    import { SoundTrack } from "@redinnlabs/system/Units";
    import { mdiPencil } from '@mdi/js';


    export let presetName: string = 'Example';

</script>

    <!-- C O N T E N T -->
    
    <div class="content">
        <!-- svelte-ignore component-name-lowercase -->
        <div class="title">
            <Heading tag={3}>{presetName}</Heading>
        </div>

        <div class="duration">
            <Heading tag={5} className="!font-normal">Duration</Heading>
            <RadioInput options={[
                { value: 'Pomodoro'}, 
                { value: 'Continues'}, 
                { value: 'Stopwatch'}
                ]}
            />
            <TimeInput/>
        </div>

        <div class="options">
            <Heading tag={5} className="!font-normal">Options</Heading>
            <CheckMultiple options= {[
                { label: 'Hard Limit', isChecked: true },
                { label: '20:20:20 Rule', isChecked: false },
                ]}
            />
        </div>

        <div class="soundtrack">
            <Heading tag={5} className="!font-normal">Soundtrack</Heading>
            {#each Array(3) as _, i }
                <SoundTrack title="Example Soundtrack" info="Chose one"/>
            {/each}
        </div>
        
        <div class="start">
            <Button>Start {presetName}</Button>
        </div>
    </div>

    <div class="back">
        <a href="/focus">
            <BackButton/>
        </a>
    </div>
    <div class="edit">
        <EditButton/>
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
            mb-14 mt-28
            gap-10
            ;
        pointer-events: all;
    }
    .content .duration {
        @apply  
            grid
            gap-3
            place-items-center
            w-full;
    }
    .content .options {
        @apply  
            grid
            gap-3
            place-items-center
            w-full;
    }
    .content .soundtrack {
        @apply  
            grid
            gap-3
            place-items-center
            w-full;
    }

</style>