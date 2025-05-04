import {ProfileFilled} from "@ant-design/icons";
import ReactiveButton from "reactive-button";

export const ViewConstellationButton = ({
                                            onOpen,
                                            isDisabled
                                        }: {
    onOpen: () => void;
    isDisabled: boolean;
}) => (
    <ReactiveButton
        size="medium"
        color="primary | dark"
        rounded
        disabled={isDisabled}
        idleText={
        <span>
            <ProfileFilled/>
            Constellation Info
        </span>
        }
        onClick={onOpen}/>
);