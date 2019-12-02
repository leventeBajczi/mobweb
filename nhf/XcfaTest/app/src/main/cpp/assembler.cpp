//
// Created by levente on 12/1/19.
//

#include "assembler.h"

MAssembler::MAssembler()
{
    this->masm = new MacroAssembler();
    this->decoder = new Decoder();
    this->simulator = new Simulator(this->decoder);
}

void MAssembler::finalize() {
    this->masm->FinalizeCode();
}

void MAssembler::run() {
    this->simulator->RunFrom(this->masm->GetLabelAddress<Instruction *>(this->firstLabel));
}

int64_t MAssembler::getReg(unsigned int i) {
    return this->simulator->ReadXRegister(i);
}

void MAssembler::bind(Label *label) {
    if(this->firstLabel == nullptr) this->firstLabel = label;
    this->masm->Bind(label);
}

MacroAssembler *MAssembler::getMasm() {
    return this->masm;
}

MAssembler::~MAssembler() {
    delete this->simulator;
    delete this->decoder;
    delete this->masm;
}

void MAssembler::addGlobal(unsigned int reg, uint64_t value) {
    this->simulator->WriteXRegister(reg, value);
}
