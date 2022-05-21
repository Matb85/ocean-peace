<script lang="ts">
  
    import "@redinnlabs/system/utils/base.css";
    import { BackButton, Button, Icon, Heading, Aquarium } from "@redinnlabs/system/Elements";
    import { Schedule, Preset } from "@redinnlabs/system/Units";
    import { TimeInput, RadioInput, CheckMultiple } from "@redinnlabs/system/Form";
    import { mdiPencil } from '@mdi/js';
    import W from "@redinnlabs/system/assets/icon-working.svg";
    import A from "@redinnlabs/system/assets/icon-add.svg";


    export let presetName: string = 'Example';

    function back () {
        location.href='/focus';
    }
</script>

    <!-- C O N T E N T -->
    
    <div class="content">
        <!-- svelte-ignore component-name-lowercase -->
        <div class="title">
            <Heading tag={3}>{presetName}</Heading>
        </div>

        <div class="duration">
            <Heading tag={5} className="font-normal">Duration</Heading>
            <RadioInput options={[
                { value: 'Pomodoro'}, 
                { value: 'Continues'}, 
                { value: 'Stopwatch'}
                ]}
            />
            <TimeInput/>
        </div>

        <div class="options">
            <Heading tag={5} className="font-normal">Options</Heading>
            <CheckMultiple options= {[
                { label: 'Hard Limit', isChecked: true },
                { label: '20:20:20 Rule', isChecked: false },
                ]}
            />
        </div>

        <div class="soundtrack">
            <Heading tag={5} className="font-normal">Soundtrack</Heading>
        </div>
        
        <div class="start">
            <Button>Start {presetName}</Button>
        </div>
    </div>

    <div class="back" htmlfor="back">
        <BackButton on:click={back}/>
    </div>
    <div class="edit">
        <Icon d={mdiPencil} fill="black" className="w-10 h-10"/>
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
            top-8 right-4
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