
// A simple component that renders a Minecraft item given a id
// Example: <mcitem id="minecraft:diamond" />

import { title } from "process";

function ItemIcon(props: { id: string }) {
    const [namespace, path] = props.id.split(':');
    return (
        <div>
            <img
                src={`/mods/${namespace}/${path}.png`}
                alt={path}
                title={path}
                style={{ width: '1em', height: '1em' }}
            />
        </div>
    );
}

// Display an image for the item, the image should be in the public folder at /mods/{namespace}/{path}.png
export function Mcitem(props: { id: string }) {
    const [namespace, path] = props.id.split(':');
    const name = titleCase(path.replaceAll('_', ' '));
    return (
        <div>
            <ItemIcon id={props.id} />
            <p>{name}</p>
        </div>
    );
}

export function Recipe(props: { recipe: string }) {
    const recipe = parseRecipe(props.recipe);
    const width = recipe.inputs.reduce((max, input) => Math.max(max, input.length), 0);
    const height = recipe.inputs.length;

    // Create a table 
    return (
        <table>
            <tbody>
                {recipe.inputs.map((input, y) => (
                    <tr key={y}>
                        {input.map((id, x) => (
                            <td key={x}>
                                <ItemIcon id={id} />
                            </td>
                        ))}
                        {Array.from({ length: width - input.length }).map((_, x) => (
                            <td key={x + input.length}></td>
                        ))}
                    </tr>
                ))}
                <tr>
                    <td colSpan={width}>
                        <ItemIcon id={recipe.output} />
                    </td>
                </tr>
            </tbody>
        </table>
    );
}

function titleCase(str: string) {
    return str.split('_').map(word => word.charAt(0).toUpperCase() + word.slice(1)).join(' ');
}

interface Recipe {
    inputs: string[][];
    output: string;
}

enum ParseState {
    INPUT,
    OUTPUT,
    UNKNOWN
}

/*
 input techreborn:steel_plate techreborn:steel_plate
techreborn:steel_plate input techreborn:advanced_circuit
techreborn:advanced_machine_frame techreborn:advanced_circuit input
techreborn:steel_plate techreborn:steel_plate techreborn:steel_plate
output techreborn:advanced_machine_casing,4 
*/
function parseRecipe(recipe: string): Recipe {
    const split = recipe.trim().split(' ');
    let inputs = [];
    let output = '';
    
    let state = ParseState.UNKNOWN;

    for (let i = 0; i < split.length; i++) {
        const word = split[i];
        if (word === 'input') {
            state = ParseState.INPUT;
            inputs.push([]);
        } else if (word === 'output') {
            state = ParseState.OUTPUT;
        } else {
            if (state === ParseState.INPUT) {
                inputs[inputs.length - 1].push(word);
            } else if (state === ParseState.OUTPUT) {
                output = word;
            }
        }
    }

    return {
        inputs: inputs,
        output
    };
}
