import React, { Component } from 'react';

import Select from 'react-select';
import createReactClass from 'create-react-class';
import { FixedSizeList as List } from "react-window";

// Solution for long lists https://github.com/JedWatson/react-select/issues/2850
// TODO: Data should be paged

const height = 35;

class MenuList extends Component {
    render() {
        const { options, children, maxHeight, getValue } = this.props;
        if (!options || options.length < 1) {
            return (<span>{this.props.noItemsMsg}</span>);
        }
        const [value] = getValue();
        const initialOffset = options.indexOf(value) * height;
    
        return (
            <List
                height={maxHeight}
                itemCount={children.length}
                itemSize={height}
                initialScrollOffset={initialOffset}
            >
            {({ index, style }) => <div style={style}>{children[index]}</div>}
            </List>
        );
    }
};

const MultiSelect = createReactClass({
    render () {
        if (this.props.error) {
            return <div>MultiSelect: {this.props.error.message}</div>;
        } else if (!this.props.isLoaded) {
            return <div>Loading {this.props.name} MultiSelect...</div>;
        } else {
            return (
                <div id="select-box">
                    <div id="label">
                        <span>{this.props.selectLabel}</span>
                    </div>
                    <Select
                        isMulti
                        name={this.props.name}
                        options={this.props.data}
                        className="basic-multi-select"
                        classNamePrefix="select"
                        components={{ MenuList }}
                        onChange={this.props.changeHandler}
                        value={this.props.selectedOptions}
                    />
                </div>
            );
        }
    }
});

export default MultiSelect