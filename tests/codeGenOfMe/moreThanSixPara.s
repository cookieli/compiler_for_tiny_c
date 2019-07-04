.file "tests/codeGenOfMe/moreThanSixPara.dcf"
.text
.comm	k,1, 1
.section .rodata
.LC0:
.string "%d,%d, %d, %d, %d %d %d %d %d %d %d\n"
.text
.globl main
.type main, @function
main:
pushq %rbp
movq %rsp, %rbp
subq $64, %rsp
subq $8,%rsp
movsbq k(%rip),%rdi
pushq %rdi
movq -11(%rbp),%rdi
pushq %rdi
movq -19(%rbp),%rdi
pushq %rdi
movsbq -1(%rbp),%rdi
pushq %rdi
movsbq -2(%rbp),%rdi
pushq %rdi
movsbq -3(%rbp),%r9
movq -27(%rbp),%r8
movq -35(%rbp),%rcx
movq -43(%rbp),%rdx
movq -51(%rbp),%rsi
leaq .LC0(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
addq $48,%rsp
leave
ret

