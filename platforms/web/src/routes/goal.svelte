<script lang="ts">

    import "@redinnlabs/system/utils/base.css";
    import { BackButton, Button, Icon, Heading, Aquarium, EditButton } from "@redinnlabs/system/Elements";
    import { PieChart } from "@redinnlabs/system/Charts";
   

</script>

    <!-- C O N T E N T -->
    <div class="aquarium">
            <Aquarium percent={80} />
    </div>
    
    <div class="content">
        

        <div class="title">
            <Heading tag={3}>Goal</Heading>
        </div>

        <div class="goal-graph">
            <Heading tag={5} className="text-center !font-normal">Limit Social Media to 90min a day</Heading>
            <div class="graph">
                <PieChart className="w-64 h-64"
                    maxValue = 200
                    data= {[
                        { color: '#3772FF', value: 110 },
                        { color: '#FCBA04', value: 40 },
                    ]}
                />
                
            </div>

            <div class="text">
                <Heading tag={5}>24 min</Heading>
                <Heading tag={5} className="!font-normal">left</Heading>
            </div>
        </div>  

        <div class="last-graph">
            <Heading tag={5} className="text-center !font-normal">Last 7 days</Heading>
            <div class="graphs">
                {#each Array(7) as _, i }
                    <PieChart className="w-24 h-24"
                        maxValue = 200
                        data= {[
                            { color: '#3772FF', value: 100 },
                            { color: '#FCBA04', value: 30 },
                        ]}
                    />
                {/each}
            </div>
        </div> 
        <div class="apps">
            <Heading tag={5} className="text-center !font-normal">Selected apps</Heading>
        </div>
    </div>

<div class="back">
    <a href="/index">
        <BackButton/>
    </a>
</div>
<div class="edit">
    <a href="/goaledit">
        <EditButton/>
    </a>
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
.title {
    @apply absolute
            top-7;
}
.aquarium {
    @apply 
        w-full h-96
        absolute
        -z-50
        top-0
        opacity-50;
}

/* C O N T E N T   E L E M E N T S */


.content {
    @apply 
        flex flex-col 
        items-center place-content-center
        select-none
        mb-16 mt-28
        gap-10
        z-10;
    pointer-events: all;
}

.content .goal-graph {
    @apply 
        grid
        grid-cols-1
        items-center
        place-items-center
        select-none
        w-11/12;
}
.content .goal-graph .graph {
    @aply 
        flex;   
}
.content .goal-graph .text{
    @aply 
        absolute;
}

.content .last-graph {
    @apply 
        grid
        grid-cols-1
        items-center
        place-items-center
        select-none
        w-11/12;
}
.content .last-graph .graphs {
    @apply 
        grid
        grid-cols-4
        items-center
        place-items-center
        justify-items-center
        select-none
        gap-2;
}

.content .apps {
    @apply 
        grid
        grid-cols-1
        items-center
        place-items-center
        select-none
        w-11/12;
}
</style>