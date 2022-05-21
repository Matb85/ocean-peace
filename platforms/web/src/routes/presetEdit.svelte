<script lang="ts">
  
    import "@redinnlabs/system/utils/base.css";
    import { BackButton, Button, Icon, Heading, Aquarium } from "@redinnlabs/system/Elements";
    import { Schedule, Preset } from "@redinnlabs/system/Units";
    import { TimeInput, RadioInput } from "@redinnlabs/system/Form";
    import { mdiPencil } from '@mdi/js';
    import W from "@redinnlabs/system/assets/icon-working.svg";
    import A from "@redinnlabs/system/assets/icon-add.svg";
</script>

    <!-- C O N T E N T -->
    
    <div class="content">
        <!-- svelte-ignore component-name-lowercase -->
        

        <RadioInput options={[{value: 'Pomodoro'}, { value: 'Continues'}, { value: 'Stopwatch'}]}/>

        <div class="title">
            <Heading tag={3}>Preset name</Heading>
        </div>
        <TimeInput></TimeInput>
    </div>

    <a href="/focus" id="back">back</a>
    <div class="back" htmlfor="back">
        <BackButton/>
    </div>
    <div class="edit">
        <Icon d={mdiPencil} fill="black" className="w-10 h-10"/>
    </div>

<style lang="postcss">
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
            gap-14
            ;
        pointer-events: all;
    }

</style>